/**
 * 상품 등록 Form 관리
 */
class ProductNewForm {
    constructor() {
        this.form = document.getElementById('productForm');
        this.optionsContainer = document.getElementById('productOptionsContainer');
        this.cards = [];
        this.counter = 0;
        this._bind();
    }

    _bind() {
        // 수동 옵션 추가
        document.getElementById('addManualOptionButton')?.addEventListener('click', () => this._addCard(false));

        // 상품 타입 토글
        document.querySelectorAll("input[name='single']").forEach(input => input.addEventListener('change', () => {
            const isOption = document.getElementById('optionProduct').checked;
            document.getElementById('optionSection').style.display = isOption ? 'block' : 'none';
            document.getElementById('singleSection').style.display = isOption ? 'none' : 'block';
        }));

        // 템플릿 불러오기
        document.getElementById('loadTemplateButton')?.addEventListener('click', async () => {
            const cid = document.getElementById('categoryId').value;
            if (!cid) {
                alert('카테고리를 먼저 선택하세요.');
                return;
            }

            const res = await fetch(`/api/categories/${cid}/options`);
            if (!res.ok) throw new Error('템플릿 로드 실패');
            const templates = await res.json();
            this._showTemplates(templates);
            new bootstrap.Modal(document.getElementById('templateModal')).show();
        });

        // 템플릿 선택
        $('#templateModalContainer').on('click', '.option-template-card', e => {
            const template = JSON.parse($(e.currentTarget).attr('data-template'));
            this._addCard(true, {
                name: template.name,
                templateId: template.id,
                optionValues: template.globalOptionValues
            });
            this.counter++;
            bootstrap.Modal.getInstance(document.getElementById('templateModal')).hide();
        });

        // 폼 제출
        this.form.addEventListener('submit', e => this._submit(e));
    }

    _addCard(isTpl, data = {}) {
        const card = new OptionCard(this.counter++, isTpl, data, removed => {
            this.cards = this.cards.filter(c => c !== removed);
        });
        this.cards.push(card);
        this.optionsContainer.append(card.element);
    }

    _showTemplates(arr) {
        const container = $('#templateModalContainer').empty();
        if (!arr.length) return container.html('<p>템플릿이 없습니다.</p>');
        arr.forEach(t => {
            container.append(`
                <div class="col">
                    <div class="card option-template-card" data-template='${JSON.stringify(t)}'>
                        <div class="card-body">
                            <h5 class="card-title">${t.name}</h5>
                            ${t.globalOptionValues?.length ? `<ul class="list-group list-group-flush">${t.globalOptionValues.map(v => `<li class="list-group-item">${v.optionValue}</li>`).join('')}</ul>` : ''}
                        </div>
                    </div>
                </div>
            `);
        });
    }

    _gather() {
        return {
            name: document.getElementById('name').value,
            description: document.getElementById('description').value,
            basePrice: document.getElementById('basePrice').value,
            manufactureDate: document.getElementById('manufactureDate').value,
            stockQuantity: document.getElementById('stockQuantity')?.value || null,
            single: document.querySelector("input[name='single']:checked").value,
            categoryId: document.getElementById('categoryId').value,
            productOptions: this.cards.map(c => c.getData())
        };
    }

    async _submit(e) {
        e.preventDefault();
        try {
            // 썸네일 및 추가 이미지 업로드
            let thumbData = null;
            const imagesData = [];

            // 썸네일 업로드 (파일이 존재하면 업로드 수행)
            const thumbInput = document.getElementById('thumbnail');
            if (thumbInput.files.length) {
                thumbData = await uploadFiles(thumbInput.files);
            }

            // 추가 이미지 업로드 (파일이 존재하면 업로드 수행)
            const imagesInput = document.getElementById('images');
            if (imagesInput.files.length > 0) {
                // 보수적 처리 (이미 onchange 시 체크하지만 중복 체크)
                if (imagesInput.files.length > 4) {
                    alert("추가 상품 이미지는 최대 4장까지만 업로드할 수 있습니다.");
                    return;
                }
                imagesData.push(...await uploadFiles(imagesInput.files));
            }

            // 본문 데이터
            const data = this._gather();
            data.thumbnail = thumbData[0];
            data.images = imagesData;
            const res = await fetch('/seller/products/new', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (!res.ok) throw new Error('상품 등록 실패');
            const result = await res.json();

            window.location.replace(`/seller/products/${result.id}`);
        } catch (err) {
            console.error(err);
            alert(err.message);
        }
    }
}

// 파일 업로드 함수
async function uploadFiles(files) {
    const fm = new FormData();
    // 모든 파일을 'files' 키에 추가
    for (const file of files) {
        fm.append('files', file);
    }

    console.log(fm);

    const res = await fetch(`/api/products/images`, {
        method: 'POST',
        body: fm
    });

    console.log(res);

    if (!res.ok) throw new Error('이미지 업로드 실패');
    return res.json(); // 업로드된 파일 데이터 반환(배열)
}


// 썸네일 이미지 미리보기
document.getElementById('thumbnail').addEventListener('change', function () {
    var preview = document.getElementById('thumbnailPreview');
    preview.innerHTML = ""; // 기존 미리보기 초기화
    if (this.files && this.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            preview.innerHTML = '<img src="' + e.target.result + '" class="preview-img" alt="썸네일 미리보기"/>';
        }
        reader.readAsDataURL(this.files[0]);
    }
});

// 추가 이미지 미리보기 및 최대 4장 제한
document.getElementById('images').addEventListener('change', function () {
    var preview = document.getElementById('imagesPreview');
    preview.innerHTML = ""; // 미리보기 영역 초기화
    if (this.files) {
        // 선택된 파일 수가 4장을 초과하면 경고 후 파일 선택 초기화
        if (this.files.length > 4) {
            alert("추가 상품 이미지는 최대 4장까지만 업로드할 수 있습니다.");
            this.value = "";
            return;
        }
        for (var i = 0; i < this.files.length; i++) {
            var file = this.files[i];
            var reader = new FileReader();
            reader.onload = function (e) {
                var img = document.createElement('img');
                img.src = e.target.result;
                img.className = "preview-img";
                img.alt = "추가 이미지 미리보기";
                preview.appendChild(img);
            }
            reader.readAsDataURL(file);
        }
    }
});


// 페이지 로드시 인스턴스화
document.addEventListener('DOMContentLoaded', () => new ProductNewForm());