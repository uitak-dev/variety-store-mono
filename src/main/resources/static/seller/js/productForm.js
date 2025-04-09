// productForm.js

/**
 * 유틸리티: DOM 요소 생성 도우미
 */
class DomHelper {
    static createElement(tag, options = {}) {
        const el = document.createElement(tag);
        if (options.classes) el.classList.add(...options.classes);
        if (options.attributes) {
            Object.entries(options.attributes).forEach(([attr, value]) => {
                el.setAttribute(attr, value);
            });
        }
        if (options.text) el.textContent = options.text;
        return el;
    }
}

/**
 * OptionValueField: 하나의 옵션 값 입력 필드 (옵션 값, 추가 가격, 재고 수량)
 * 단일 책임 원칙에 따라 UI 생성, 데이터 수집, 이벤트 핸들링 역할을 전담합니다.
 */
class OptionValueField {
    constructor(readOnly = false, data = {}, onRemoveCallback = null) {
        this.readOnly = readOnly;
        this.data = data;
        this.onRemoveCallback = onRemoveCallback;
        this._build();
    }
    _build() {
        this.element = DomHelper.createElement('div', { classes: ['row', 'g-2', 'mb-3', 'align-items-end', 'option-value-field'] });
        // 옵션 값 입력 그룹
        const valueGroup = DomHelper.createElement('div', { classes: ['col-md-4'] });
        const valueLabel = DomHelper.createElement('label', { classes: ['form-label'], text: '옵션 값' });
        this.optionValueInput = DomHelper.createElement('input', { classes: ['form-control'] });
        this.optionValueInput.type = 'text';
        this.optionValueInput.placeholder = '옵션 값...';
        this.optionValueInput.required = true;
        if (this.readOnly) {
            this.optionValueInput.readOnly = true;
            this.optionValueInput.classList.add('read-only');
        }
        this.optionValueInput.value = this.data.optionValue || '';
        valueGroup.append(valueLabel, this.optionValueInput);

        // 추가 가격 입력 그룹
        const priceGroup = DomHelper.createElement('div', { classes: ['col-md-4'] });
        const priceLabel = DomHelper.createElement('label', { classes: ['form-label'], text: '추가 가격' });
        this.additionalPriceInput = DomHelper.createElement('input', { classes: ['form-control'] });
        this.additionalPriceInput.type = 'number';
        this.additionalPriceInput.placeholder = '추가 가격...';
        this.additionalPriceInput.required = true;
        this.additionalPriceInput.value = this.data.additionalPrice || '';
        priceGroup.append(priceLabel, this.additionalPriceInput);

        // 재고 수량 입력 그룹
        const stockGroup = DomHelper.createElement('div', { classes: ['col-md-3'] });
        const stockLabel = DomHelper.createElement('label', { classes: ['form-label'], text: '재고 수량' });
        this.stockQuantityInput = DomHelper.createElement('input', { classes: ['form-control'] });
        this.stockQuantityInput.type = 'number';
        this.stockQuantityInput.placeholder = '재고 수량...';
        this.stockQuantityInput.required = true;
        this.stockQuantityInput.value = this.data.stockQuantity || '';
        stockGroup.append(stockLabel, this.stockQuantityInput);

        // 삭제 버튼 그룹
        const deleteGroup = DomHelper.createElement('div', { classes: ['col-md-1'] });
        this.deleteButton = DomHelper.createElement('button', { classes: ['btn', 'btn-danger', 'btn-sm', 'w-100'], text: '삭제' });
        this.deleteButton.type = 'button';
        this.deleteButton.addEventListener('click', () => this.remove());
        deleteGroup.append(this.deleteButton);

        this.element.append(valueGroup, priceGroup, stockGroup, deleteGroup);
    }
    getData() {
        return {
            optionValue: this.optionValueInput.value,
            additionalPrice: this.additionalPriceInput.value,
            stockQuantity: this.stockQuantityInput.value,
            global: this.readOnly, // 템플릿 기반 여부 플래그
            globalOptionValueId: this.data.id
        };
    }
    remove() {
        this.element.remove();
        if (typeof this.onRemoveCallback === 'function') {
            this.onRemoveCallback(this);
        }
    }
}

/**
 * OptionCard: 하나의 옵션 카드 (옵션 이름 및 옵션 값 필드들의 집합)
 * 이 클래스는 카드 구성, 이벤트 처리, 데이터 수집 기능에 집중합니다.
 */
class OptionCard {
    constructor(index, isTemplate = false, data = {}, onRemoveCallback = null) {
        this.index = index;
        this.isTemplate = isTemplate;
        this.data = data; // { name, optionValues, templateId }
        this.onRemoveCallback = onRemoveCallback;
        this.optionValueFields = [];
        this._build();
    }
    _build() {
        this.element = DomHelper.createElement('div', { classes: ['card', 'mb-3', 'option-card'] });
        const cardBody = DomHelper.createElement('div', { classes: ['card-body'] });
        // 옵션 이름 입력 그룹
        const nameGroup = DomHelper.createElement('div', { classes: ['mb-3'] });
        const nameLabel = DomHelper.createElement('label', { classes: ['form-label'], text: '옵션 이름' });
        this.optionNameInput = DomHelper.createElement('input', { classes: ['form-control'] });
        this.optionNameInput.type = 'text';
        this.optionNameInput.placeholder = '옵션 이름...';
        this.optionNameInput.required = true;
        this.optionNameInput.value = this.data.name || '';
        if (this.isTemplate) {
            this.optionNameInput.readOnly = true;
            this.optionNameInput.classList.add('read-only');
        }
        nameGroup.append(nameLabel, this.optionNameInput);

        // 옵션 값들을 담을 컨테이너
        this.optionValuesContainer = DomHelper.createElement('div', { classes: ['option-values'] });
        if (this.data.optionValues && this.data.optionValues.length > 0) {
            this.data.optionValues.forEach(valData => {
                this.addOptionValueField(this.isTemplate, valData);
            });
        } else {
            // 기본 필드 1개 추가
            this.addOptionValueField();
        }

        // 버튼 그룹 (옵션 값 추가, 옵션 카드 삭제)
        const buttonGroup = DomHelper.createElement('div', { classes: ['d-flex', 'gap-2'] });
        this.addValueBtn = DomHelper.createElement('button', { classes: ['btn', 'btn-outline-primary', 'btn-sm'], text: '옵션 값 추가' });
        this.addValueBtn.type = 'button';
        this.addValueBtn.addEventListener('click', () => this.addOptionValueField());
        this.deleteCardBtn = DomHelper.createElement('button', { classes: ['btn', 'btn-danger', 'btn-sm'], text: '옵션 카드 삭제' });
        this.deleteCardBtn.type = 'button';
        this.deleteCardBtn.addEventListener('click', () => this.remove());
        buttonGroup.append(this.addValueBtn, this.deleteCardBtn);

        cardBody.append(nameGroup, this.optionValuesContainer, buttonGroup);
        this.element.appendChild(cardBody);
    }
    addOptionValueField(readOnly = false, data = {}) {
        const field = new OptionValueField(readOnly, data, (removedField) => {
            this.optionValueFields = this.optionValueFields.filter(f => f !== removedField);
            this._toggleRemoveButtons();
        });
        this.optionValueFields.push(field);
        this.optionValuesContainer.appendChild(field.element);
        this._toggleRemoveButtons();
    }
    _toggleRemoveButtons() {
        // 모든 옵션 값 필드에서 삭제 버튼을 show/hide 처리 (항목이 2개 이상이면 모두 보이게)
        const shouldShow = this.optionValueFields.length > 1;
        this.optionValueFields.forEach(field => {
            if (shouldShow) {
                field.deleteButton.classList.remove('d-none');
            } else {
                field.deleteButton.classList.add('d-none');
            }
        });
    }
    getData() {
        return {
            name: this.optionNameInput.value,
            optionValues: this.optionValueFields.map(field => field.getData()),
            global: this.isTemplate,
            globalOptionId: this.data.templateId
        };
    }
    remove() {
        this.element.remove();
        if (typeof this.onRemoveCallback === 'function') {
            this.onRemoveCallback(this);
        }
    }
}

/**
 * ProductForm: 전체 상품 폼 관리 클래스
 * 단일 책임 원칙에 따라 폼 초기화, 이벤트 바인딩, 데이터 수집 및 전송에 집중합니다.
 */
class ProductForm {
    constructor() {
        this.form = document.getElementById('productForm');
        this.optionsContainer = document.getElementById('productOptionsContainer');
        this.optionCards = [];
        this.optionCardCounter = 0;
        this.mode = this.form.dataset.mode; // "new" 또는 "edit"
        this._init();
        if (this.mode === 'edit') {
            this._initializeOptionCardsFromDOM();
        }
    }
    _init() {
        this._attachEventListeners();
    }
    _attachEventListeners() {
        // 수동 옵션 카드 추가 버튼 (존재하는 경우)
        const addManualOptionBtn = document.getElementById('addManualOptionButton');
        if (addManualOptionBtn) {
            addManualOptionBtn.addEventListener('click', () => this.addOptionCard(false));
        }
        // 폼 제출 처리
        this.form.addEventListener('submit', (e) => this._handleSubmit(e));

        // 상품 타입 변경 이벤트 (옵션 상품 선택 시 옵션 섹션 표시)
        document.querySelectorAll("input[name='single']").forEach(input => {
            input.addEventListener('change', () => {
                const isOption = document.getElementById('optionProduct').checked;
                if (isOption) {
                    const categoryId = document.getElementById('categoryId').value;
                    if (!categoryId) {
                        alert("상품이 포함될 카테고리를 먼저 선택해주세요.");
                        document.getElementById('singleProduct').checked = true;
                        document.getElementById('optionSection').style.display = 'none';
                        return;
                    }
                    document.getElementById('optionSection').style.display = 'block';
                    document.getElementById('singleSection').style.display = 'none';
                } else {
                    document.getElementById('optionSection').style.display = 'none';
                    document.getElementById('singleSection').style.display = 'block';
                }
            });
        });

        // 템플릿 불러오기 버튼 이벤트 (AJAX)
        const loadTemplateBtn = document.getElementById('loadTemplateButton');
        if (loadTemplateBtn) {
            loadTemplateBtn.addEventListener('click', async () => {
                const categoryId = document.getElementById('categoryId').value;
                if (!categoryId) {
                    alert("옵션 템플릿을 불러오려면 먼저 카테고리를 선택하세요.");
                    return;
                }
                try {
                    const response = await fetch(`/api/categories/${categoryId}/options`);
                    if (!response.ok) throw new Error("네트워크 오류: " + response.status);
                    const templates = await response.json();
                    this._updateTemplateModalUI(templates);
                    const templateModal = new bootstrap.Modal(document.getElementById('templateModal'));
                    templateModal.show();
                } catch (error) {
                    console.error("글로벌 옵션 템플릿 로드 실패:", error);
                }
            });
        }
        // 모달 내 템플릿 카드 클릭 이벤트 (jQuery 사용)
        $("#templateModalContainer").on("click", ".option-template-card", (e) => {
            const template = JSON.parse($(e.currentTarget).attr("data-template"));
            this.addOptionCard(true, {
                name: template.name,
                templateId: template.id,
                optionValues: template.globalOptionValues
            });
            this.optionCardCounter++;
            const templateModal = bootstrap.Modal.getInstance(document.getElementById('templateModal'));
            templateModal.hide();
        });
    }
    addOptionCard(isTemplate, data = {}) {
        const card = new OptionCard(this.optionCardCounter, isTemplate, data, (removedCard) => {
            this.optionCards = this.optionCards.filter(c => c !== removedCard);
        });
        this.optionCards.push(card);
        this.optionsContainer.appendChild(card.element);
        this.optionCardCounter++;
    }
    _initializeOptionCardsFromDOM() {
        // 서버 렌더링된 옵션 카드들을 OptionCard 객체로 변환하여 내부 모델에 포함
        const existingCards = this.optionsContainer.querySelectorAll('.option-card');
        existingCards.forEach(cardEl => {
            const nameInput = cardEl.querySelector('input[type="text"].form-control');
            const optionName = nameInput ? nameInput.value : '';
            const isTemplate = nameInput ? nameInput.readOnly : false;
            let optionValues = [];
            const optionValueRows = cardEl.querySelectorAll('.option-values .option-value-field');
            optionValueRows.forEach(row => {
                const inputs = row.querySelectorAll('input');
                optionValues.push({
                    optionValue: inputs[0] ? inputs[0].value : '',
                    additionalPrice: inputs[1] ? inputs[1].value : '',
                    stockQuantity: inputs[2] ? inputs[2].value : '',
                    global: inputs[0] ? inputs[0].readOnly : false
                });
            });
            const data = { name: optionName, optionValues: optionValues, templateId: cardEl.getAttribute('data-template-id') || null };
            const newCard = new OptionCard(this.optionCardCounter, isTemplate, data, (removedCard) => {
                this.optionCards = this.optionCards.filter(card => card !== removedCard);
            });
            cardEl.parentNode.replaceChild(newCard.element, cardEl);
            this.optionCards.push(newCard);
            this.optionCardCounter++;
        });
    }
    _gatherData() {
        const productData = {
            name: document.getElementById('name').value,
            description: document.getElementById('description').value,
            basePrice: document.getElementById('basePrice').value,
            manufactureDate: document.getElementById('manufactureDate').value,
            stockQuantity: document.getElementById('stockQuantity') ? document.getElementById('stockQuantity').value : null,
            single: document.querySelector('input[name="single"]:checked').value,
            categoryId: document.getElementById('categoryId').value,
            productOptions: this.optionCards.map(card => card.getData())
        };
        if (this.mode === 'edit') {
            const idInput = this.form.querySelector('input[name="id"]');
            if (idInput && idInput.value) productData.id = idInput.value;
        }
        return productData;
    }
    _handleSubmit(e) {
        e.preventDefault();
        const data = this._gatherData();
        console.log("전송 데이터:", JSON.stringify(data, null, 2));
        let url = '/seller/products/new';
        if (this.mode === 'edit') {
            const idInput = this.form.querySelector('input[name="id"]');
            if (idInput && idInput.value) {
                url = `/seller/products/${idInput.value}/edit`;
            } else {
                console.error("상품 ID를 찾을 수 없습니다.");
            }
        }
        fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) throw new Error('서버 응답 실패');
                return response.json();
            })
            .then(result => {
                console.log('전송 성공:', result);
                window.location.replace(`/seller/products/${result.id}`);
            })
            .catch(error => {
                console.error('전송 중 오류 발생:', error);
            });
    }
    _updateTemplateModalUI(templates) {
        const container = $("#templateModalContainer");
        container.empty();
        if (!templates || templates.length === 0) {
            container.html("<p>연관된 옵션 템플릿이 없습니다.</p>");
            return;
        }
        templates.forEach(template => {
            const cardHtml = `
                <div class="col">
                    <div class="card option-template-card" data-template='${JSON.stringify(template)}'>
                        <div class="card-body">
                            <h5 class="card-title">${template.name}</h5>
                            ${(template.globalOptionValues && template.globalOptionValues.length > 0)
                    ? `<ul class="list-group list-group-flush">
                                    ${template.globalOptionValues.map(val => `<li class="list-group-item">${val.optionValue}</li>`).join('')}
                                   </ul>`
                    : ""}
                        </div>
                    </div>
                </div>
            `;
            container.append(cardHtml);
        });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new ProductForm();
});
