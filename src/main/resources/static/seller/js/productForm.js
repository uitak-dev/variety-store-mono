/**
 * DOM 헬퍼: 요소 생성, 속성 설정 등을 단순화
 */
class DomHelper {
    static createElement(tag, options = {}) {
        const el = document.createElement(tag);
        if (options.classes) el.classList.add(...options.classes);
        if (options.attributes) Object.entries(options.attributes).forEach(([attr, value]) => el.setAttribute(attr, value));
        if (options.text) el.textContent = options.text;
        return el;
    }
}

/**
 * 옵션 값 입력 필드
 */
class OptionValueField {
    constructor(readOnly = false, data = {}, onRemove) {
        this.readOnly = readOnly;
        this.data = data;
        this.onRemove = onRemove;
        this._build();
    }

    _build() {
        this.element = DomHelper.createElement('div', { classes: ['row', 'g-2', 'mb-3', 'align-items-end', 'option-value-field'] });

        // 옵션 값
        const groupValue = DomHelper.createElement('div', { classes: ['col-md-4'] });
        this.inputValue = DomHelper.createElement('input', { classes: ['form-control'] });
        Object.assign(this.inputValue, {
            type: 'text',
            required: true,
            placeholder: '옵션 값...',
            readOnly: this.readOnly,
            value: this.data.optionValue || ''
        });
        groupValue.append(DomHelper.createElement('label', { classes: ['form-label'], text: '옵션 값' }), this.inputValue);

        // 추가 가격
        const groupPrice = DomHelper.createElement('div', { classes: ['col-md-4'] });
        this.inputPrice = DomHelper.createElement('input', { classes: ['form-control'] });
        Object.assign(this.inputPrice, {
            type: 'number',
            required: true,
            placeholder: '추가 가격...',
            value: this.data.additionalPrice || ''
        });
        groupPrice.append(DomHelper.createElement('label', { classes: ['form-label'], text: '추가 가격' }), this.inputPrice);

        // 재고 수량
        const groupStock = DomHelper.createElement('div', { classes: ['col-md-3'] });
        this.inputStock = DomHelper.createElement('input', { classes: ['form-control'] });
        Object.assign(this.inputStock, {
            type: 'number',
            required: true,
            placeholder: '재고 수량...',
            value: this.data.stockQuantity || ''
        });
        groupStock.append(DomHelper.createElement('label', { classes: ['form-label'], text: '재고 수량' }), this.inputStock);

        // 삭제 버튼
        const groupDel = DomHelper.createElement('div', { classes: ['col-md-1'] });
        this.btnDelete = DomHelper.createElement('button', { classes: ['btn', 'btn-danger', 'btn-sm', 'w-100'], text: '삭제' });
        this.btnDelete.type = 'button';
        this.btnDelete.addEventListener('click', () => this._remove());
        groupDel.append(this.btnDelete);

        this.element.append(groupValue, groupPrice, groupStock, groupDel);
    }

    getData() {
        return {
            optionValue: this.inputValue.value,
            additionalPrice: this.inputPrice.value,
            stockQuantity: this.inputStock.value,
            global: this.readOnly,
            globalOptionValueId: this.data.id
        };
    }

    _remove() {
        this.element.remove();
        this.onRemove?.(this);
    }
}

/**
 * 옵션 카드: 옵션 이름 + 여러 옵션 값 필드
 */
class OptionCard {
    constructor(index, isTemplate = false, data = {}, onRemove) {
        this.index = index;
        this.isTemplate = isTemplate;
        this.data = data;
        this.onRemove = onRemove;
        this.fields = [];
        this._build();
    }

    _build() {
        this.element = DomHelper.createElement('div', { classes: ['card', 'mb-3', 'option-card'] });
        const body = DomHelper.createElement('div', { classes: ['card-body'] });

        // 옵션 이름
        const nameGroup = DomHelper.createElement('div', { classes: ['mb-3'] });
        this.inputName = DomHelper.createElement('input', { classes: ['form-control'] });
        Object.assign(this.inputName, {
            type: 'text', required: true,
            placeholder: '옵션 이름...',
            readOnly: this.isTemplate,
            value: this.data.name || ''
        });
        nameGroup.append(DomHelper.createElement('label', { classes: ['form-label'], text: '옵션 이름' }), this.inputName);

        // 옵션 값 컨테이너
        this.valuesContainer = DomHelper.createElement('div', { classes: ['option-values'] });
        (this.data.optionValues || [{}]).forEach(val => this._addField(this.isTemplate, val));

        // 버튼 그룹
        const btnGroup = DomHelper.createElement('div', { classes: ['d-flex', 'gap-2'] });

        const btnAdd = DomHelper.createElement('button', { classes: ['btn', 'btn-outline-primary', 'btn-sm'], text: '옵션 값 추가' });
        btnAdd.type = 'button';
        btnAdd.addEventListener('click', () => this._addField());
        const btnRemove = DomHelper.createElement('button', { classes: ['btn', 'btn-danger', 'btn-sm'], text: '옵션 카드 삭제' });
        btnRemove.type = 'button';
        btnRemove.addEventListener('click', () => this._remove());
        btnGroup.append(btnAdd, btnRemove);

        body.append(nameGroup, this.valuesContainer, btnGroup);
        this.element.append(body);
    }

    _addField(readOnly = false, data = {}) {
        const field = new OptionValueField(readOnly, data, removed => {
            this.fields = this.fields.filter(f => f !== removed);
            this._toggleDeleteButtons();
        });
        this.fields.push(field);
        this.valuesContainer.append(field.element);
        this._toggleDeleteButtons();
    }

    _toggleDeleteButtons() {
        const show = this.fields.length > 1;
        this.fields.forEach(f => f.btnDelete.classList[show ? 'remove' : 'add']('d-none'));
    }

    getData() {
        return {
            name: this.inputName.value,
            optionValues: this.fields.map(f => f.getData()),
            global: this.isTemplate,
            globalOptionId: this.data.templateId
        };
    }

    _remove() {
        this.element.remove();
        this.onRemove?.(this);
    }
}
