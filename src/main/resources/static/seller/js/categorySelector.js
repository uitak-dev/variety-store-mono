/**
 * CategorySelector: 카테고리 선택 모달
 */
class CategorySelector {
    constructor({
        modalId,
        listContainerId,
        breadcrumbContainerId,
        confirmBtnId,
        goBackBtnId,
        selectedDisplayId,
        hiddenInputId
    }) {
        this.modal = document.getElementById(modalId);
        this.listContainer = document.getElementById(listContainerId);
        this.breadcrumbContainer = document.getElementById(breadcrumbContainerId);
        this.confirmBtn = document.getElementById(confirmBtnId);
        this.goBackBtn = document.getElementById(goBackBtnId);
        this.selectedDisplay = document.getElementById(selectedDisplayId);
        this.hiddenInput = document.getElementById(hiddenInputId);
        this.currentParentId = null;
        this.breadcrumbStack = [];
        this._bindEvents();
    }

    _bindEvents() {
        // 모달 열릴 때 초기화
        this.modal.addEventListener('shown.bs.modal', () => this._initModal());
        // 뒤로가기
        this.goBackBtn.addEventListener('click', () => this._onGoBack());
        // 선택 완료
        this.confirmBtn.addEventListener('click', () => this._onConfirm());
    }

    _initModal() {
        this.breadcrumbStack = [];
        this.currentParentId = null;
        this._renderBreadcrumb();
        this._loadCategories();
        this.confirmBtn.disabled = true;
    }

    async _loadCategories(parentId = null) {
        this.listContainer.innerHTML = '';
        this.confirmBtn.disabled = true;
        const url = parentId
            ? `/api/categories/children?categoryId=${parentId}`
            : '/api/categories/children';
        try {
            const res = await fetch(url);
            const categories = await res.json();
            if (!categories.length) {
                this.listContainer.innerHTML = '<p>더 이상 하위 카테고리가 없습니다.</p>';
                this.confirmBtn.disabled = false;
            } else {
                const ul = document.createElement('ul');
                ul.classList.add('list-group');
                categories.forEach(cat => {
                    const li = document.createElement('li');
                    li.classList.add('list-group-item', 'list-group-item-action');
                    li.textContent = cat.name;
                    li.style.cursor = 'pointer';
                    li.addEventListener('click', () => this._onCategoryClick(cat));
                    ul.appendChild(li);
                });
                this.listContainer.appendChild(ul);
            }
        } catch (err) {
            console.error('카테고리 로드 실패', err);
        }
    }

    _onCategoryClick(cat) {
        this.currentParentId = cat.id;
        this.breadcrumbStack.push({ id: cat.id, name: cat.name });
        this._renderBreadcrumb();
        this._loadCategories(cat.id);
    }

    _renderBreadcrumb() {
        this.breadcrumbContainer.innerHTML = '';
        this.breadcrumbStack.forEach((item, idx) => {
            const span = document.createElement('span');
            span.textContent = item.name;
            span.style.cursor = 'pointer';
            span.addEventListener('click', () => this._onBreadcrumbClick(idx));
            this.breadcrumbContainer.appendChild(span);
            if (idx < this.breadcrumbStack.length - 1) {
                const sep = document.createElement('span');
                sep.textContent = ' > ';
                this.breadcrumbContainer.appendChild(sep);
            }
        });
    }

    _onBreadcrumbClick(index) {
        this.breadcrumbStack = this.breadcrumbStack.slice(0, index + 1);
        this.currentParentId = this.breadcrumbStack[index].id;
        this._renderBreadcrumb();
        this._loadCategories(this.currentParentId);
    }

    _onGoBack() {
        if (this.breadcrumbStack.length) {
            this.breadcrumbStack.pop();
            this.currentParentId = this.breadcrumbStack.length
                ? this.breadcrumbStack[this.breadcrumbStack.length - 1].id
                : null;
            this._renderBreadcrumb();
            this._loadCategories(this.currentParentId);
        } else {
            this._loadCategories();
        }
    }

    _onConfirm() {
        const selected = this.breadcrumbStack.length
            ? this.breadcrumbStack[this.breadcrumbStack.length - 1] : null;
        if (selected) {
            this.selectedDisplay.value = this.breadcrumbStack.map(i => i.name).join(' > ');
            this.hiddenInput.value = selected.id;
        } else {
            this.selectedDisplay.value = '';
            this.hiddenInput.value = '';
        }
        const modalInstance = bootstrap.Modal.getInstance(this.modal);
        modalInstance.hide();
    }
}