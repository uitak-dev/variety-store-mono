// productForm.js

// OptionValueField: 하나의 옵션 값 입력 필드 (옵션 값, 추가 가격, 재고 수량)
class OptionValueField {
    constructor(readOnly = false, data = {}, onRemoveCallback = null) {
        this.readOnly = readOnly;
        this.data = data;
        this.element = document.createElement('div');
        this.element.classList.add('row', 'g-2', 'mb-3', 'align-items-end', 'option-value-field');   //##
        this.onRemoveCallback = onRemoveCallback; // 삭제 시 호출할 콜백
        this.build();
    }
    build() {
        // 옵션 값 인풋 그룹
        const valueGroup = document.createElement('div');
        valueGroup.classList.add('col-md-4');

        const valueLabel = document.createElement('label');
        valueLabel.classList.add('form-label');
        valueLabel.textContent = '옵션 값';

        this.optionValueInput = document.createElement('input');
        this.optionValueInput.type = 'text';
        this.optionValueInput.classList.add('form-control');
        this.optionValueInput.placeholder = '옵션 값...';
        this.optionValueInput.required = true;
        if (this.readOnly) {
            this.optionValueInput.readOnly = true;
            this.optionValueInput.classList.add('read-only');
        }
        this.optionValueInput.value = this.data.optionValue || '';
        valueGroup.append(valueLabel, this.optionValueInput);

        // 추가 가격 인풋 그룹
        const priceGroup = document.createElement('div');
        priceGroup.classList.add('col-md-4');

        const priceLabel = document.createElement('label');
        priceLabel.classList.add('form-label');
        priceLabel.textContent = '추가 가격';

        this.additionalPriceInput = document.createElement('input');
        this.additionalPriceInput.type = 'number';
        this.additionalPriceInput.classList.add('form-control');
        this.additionalPriceInput.placeholder = '추가 가격...';
        this.additionalPriceInput.required = true;
        this.additionalPriceInput.value = this.data.additionalPrice || '';
        priceGroup.append(priceLabel, this.additionalPriceInput);

        // 재고 수량 인풋 그룹
        const stockGroup = document.createElement('div');
        stockGroup.classList.add('col-md-3');

        const stockLabel = document.createElement('label');
        stockLabel.classList.add('form-label');
        stockLabel.textContent = '재고 수량';

        this.stockQuantityInput = document.createElement('input');
        this.stockQuantityInput.type = 'number';
        this.stockQuantityInput.classList.add('form-control');
        this.stockQuantityInput.placeholder = '재고 수량...';
        this.stockQuantityInput.required = true;
        this.stockQuantityInput.value = this.data.stockQuantity || '';
        stockGroup.append(stockLabel, this.stockQuantityInput);

        // 삭제 버튼
        const deleteGroup = document.createElement('div');
        deleteGroup.classList.add('col-md-1');

        this.deleteButton = document.createElement('button');
        this.deleteButton.type = 'button';
        this.deleteButton.textContent = '삭제';
        this.deleteButton.classList.add('btn', 'btn-danger', 'btn-sm', 'w-100');
        this.deleteButton.addEventListener('click', () => this.remove());
        deleteGroup.append(this.deleteButton);

        this.element.append(valueGroup, priceGroup, stockGroup, deleteGroup);
    }
    getData() {
        return {
            optionValue: this.optionValueInput.value,
            additionalPrice: this.additionalPriceInput.value,
            stockQuantity: this.stockQuantityInput.value,
            global: this.readOnly, // readOnly flag로 템플릿 기반 여부를 구분
            globalOptionValueId: this.data.id
        };
    }
    remove() {
        this.element.remove();
        if (this.onRemoveCallback && typeof this.onRemoveCallback === 'function') {
            this.onRemoveCallback(this);
        }
    }
}

// OptionCard: 하나의 옵션 카드 (옵션 이름과 여러 옵션 값 필드)
class OptionCard {
    constructor(index, isTemplate = false, data = {}, onRemoveCallback = null) {
        this.index = index;
        this.isTemplate = isTemplate;
        this.data = data; // { name, optionValues, templateId }
        this.optionValueFields = [];
        this.onRemoveCallback = onRemoveCallback; // 삭제 시 호출할 콜백
        this.element = document.createElement('div');
        this.element.classList.add('card', 'mb-3', 'option-card');
        this.build();
    }
    build() {
        const cardBody = document.createElement('div');
        cardBody.classList.add('card-body');

        // 옵션 이름 입력 필드
        const nameGroup = document.createElement('div');
        nameGroup.classList.add('mb-3');

        const nameLabel = document.createElement('label');
        nameLabel.classList.add('form-label');
        nameLabel.textContent = '옵션 이름';

        this.optionNameInput = document.createElement('input');
        this.optionNameInput.classList.add('form-control'); //##
        this.optionNameInput.type = 'text';
        this.optionNameInput.placeholder = '옵션 이름...';
        this.optionNameInput.required = true;
        this.optionNameInput.value = this.data.name || '';
        if (this.isTemplate) {
            this.optionNameInput.readOnly = true;
            this.optionNameInput.classList.add('read-only');
        }
        nameGroup.append(nameLabel, this.optionNameInput);

        // 옵션 값 필드 컨테이너
        this.optionValuesContainer = document.createElement('div');
        this.optionValuesContainer.classList.add('option-values');

        // 기존 옵션 값들 추가 (템플릿 기반이면 기본값 read-only)
        if (this.data.optionValues && this.data.optionValues.length > 0) {
            this.data.optionValues.forEach(valData => {
                const field = new OptionValueField(this.isTemplate, valData, (fieldInstance) => {
                    this.optionValueFields = this.optionValueFields.filter(f => f !== fieldInstance);
                });
                this.optionValueFields.push(field);
                this.optionValuesContainer.appendChild(field.element);
            });
        } else {
            // 기본으로 한 개 추가
            this.addOptionValueField();
        }

        // 옵션 값 추가/ 옵션 카드 삭제 버튼
        const buttonGroup = document.createElement('div');
        buttonGroup.classList.add('d-flex', 'gap-2');

        this.addValueBtn = document.createElement('button');
        this.addValueBtn.type = 'button';
        this.addValueBtn.textContent = '옵션 값 추가';
        this.addValueBtn.classList.add('btn', 'btn-outline-primary', 'btn-sm');
        this.addValueBtn.addEventListener('click', () => this.addOptionValueField());

        this.deleteCardBtn = document.createElement('button');
        this.deleteCardBtn.type = 'button';
        this.deleteCardBtn.textContent = '옵션 카드 삭제';
        this.deleteCardBtn.classList.add('btn', 'btn-danger', 'btn-sm');
        this.deleteCardBtn.addEventListener('click', () => this.remove());

        buttonGroup.append(this.addValueBtn, this.deleteCardBtn);

        cardBody.append(nameGroup, this.optionValuesContainer, buttonGroup);
        this.element.appendChild(cardBody);
    }
    addOptionValueField() {
        const field = new OptionValueField(false, {}, (fieldInstance) => {
            this.optionValueFields = this.optionValueFields.filter(f => f !== fieldInstance);
        });
        this.optionValueFields.push(field);
        this.optionValuesContainer.appendChild(field.element);
    }
    getData() {
        const values = this.optionValueFields.map(field => field.getData());
        return {
            name: this.optionNameInput.value,
            optionValues: values,
            global: this.isTemplate,
            globalOptionId: this.data.templateId
        };
    }
    remove() {
        this.element.remove();
        // 내부 데이터 모델에서도 제거를 위해 콜백 호출
        if (this.onRemoveCallback && typeof this.onRemoveCallback === 'function') {
            this.onRemoveCallback(this);
        }
    }
}

// ProductForm: 전체 폼 관리 (옵션 카드 추가 및 JSON 데이터 전송)
class ProductForm {
    constructor() {
        this.form = document.getElementById('productForm');
        this.optionsContainer = document.getElementById('productOptionsContainer');
        this.optionCards = [];
        this.optionCardCounter = 0;
        this.init();
    }
    init() {
        // 옵션 카드 추가 버튼 (수동 옵션)
        document.getElementById('addManualOptionButton').addEventListener('click', () => {
            this.addOptionCard(false);
        });

        // 폼 제출 시 JSON 데이터 수집 및 전송
        this.form.addEventListener('submit', (e) => this.handleSubmit(e));

        // 상품 타입 라디오 버튼 이벤트: 옵션 상품 선택 시 옵션 영역 표시
        $("input[name='single']").on("change", () => {
            if ($("#optionProduct").is(":checked")) {
                const categoryId = $("#categoryId").val();
                if (!categoryId) {
                    alert("상품이 포함될 카테고리를 먼저 선택해주세요.");
                    $("#singleProduct").prop("checked", true);
                    $("#optionSection").hide();
                    return;
                }
                $("#optionSection").show();
                $("#singleSection").hide();
            } else {
                $("#optionSection").hide();
                $("#singleSection").show();
            }
        });
        // 템플릿 불러오기 버튼 이벤트: AJAX로 템플릿 목록 로드 후 모달 표시
        $("#loadTemplateButton").on("click", async () => {
            const categoryId = $("#categoryId").val();
            if (!categoryId) {
                alert("옵션 템플릿을 불러오려면 먼저 최하위 카테고리를 선택하세요.");
                return;
            }
            try {
                const response = await fetch(`/api/categories/${categoryId}/options`);
                if (!response.ok) throw new Error("네트워크 오류: " + response.status);
                const templates = await response.json();
                this.updateTemplateModalUI(templates);
                const templateModal = new bootstrap.Modal(document.getElementById('templateModal'));
                templateModal.show();
            } catch (error) {
                console.error("글로벌 옵션 템플릿 로드 실패:", error);
            }
        });
        // 모달 내 템플릿 카드 클릭 이벤트
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
            // 콜백: 삭제된 카드 인스턴스를 내부 배열에서 제거
            this.optionCards = this.optionCards.filter(card => card !== removedCard);
        });
        this.optionCards.push(card);
        this.optionsContainer.appendChild(card.element);
        this.optionCardCounter++;
    }
    gatherData() {
        const productData = {
            name: document.getElementById('name').value,
            description: document.getElementById('description').value,
            basePrice: document.getElementById('basePrice').value,
            manufactureDate: document.getElementById('manufactureDate').value,
            stockQuantity: document.getElementById('stockQuantity').value,
            single: document.querySelector('input[name="single"]:checked').value,
            categoryId: document.getElementById('categoryId').value,
            // 나머지 기본 정보도 추가 가능...
            productOptions: this.optionCards.map(card => card.getData())
        };
        return productData;
    }
    handleSubmit(e) {
        e.preventDefault();
        const data = this.gatherData();
        console.log("전송 데이터:", JSON.stringify(data, null, 2));
        fetch('/seller/products/new', {
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
                // 성공 시, 리다이렉트 처리(뒤로가기 히스토리를 남기지 않음)
                window.location.replace(`/seller/products/${result.id}`);
            })
            .catch(error => {
                console.error('전송 중 오류 발생:', error);
            });
    }

    updateTemplateModalUI(templates) {
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
