// 사이드바 트리 메뉴 클릭 시 active 클래스 토글
document.querySelectorAll('.tree-menu a').forEach(link => {
    link.addEventListener('click', function () {
        if (this.getAttribute('href').startsWith('#')) {
            document.querySelectorAll('.tree-menu a').forEach(item => {
                item.classList.remove('active');
            });
            this.classList.add('active');
        }
    });
});

// 좌측 사이드바(카테고리 목록) 동적 구성
document.addEventListener('DOMContentLoaded', () => {
	// 사이드바가 없는 페이지라면 아무 것도 하지 않음
	const container = document.getElementById('category-tree');
	if (!container) return;

	const apiUrl = '/api/categories';

	function buildTree(nodes) {
		const ul = document.createElement('ul');
		for (const node of nodes) {
			const li = document.createElement('li');
			if (node.children && node.children.length > 0) {
				const toggle = document.createElement('a');
				const collapseId = `category-${node.id}`;
				toggle.classList.add('collapsed');
				toggle.setAttribute('data-bs-toggle', 'collapse');
				toggle.setAttribute('href', `#${collapseId}`);
				toggle.setAttribute('aria-controls', collapseId);
				toggle.textContent = node.name;
				li.appendChild(toggle);

				const collapseDiv = document.createElement('div');
				collapseDiv.classList.add('collapse');
				collapseDiv.id = collapseId;
				collapseDiv.appendChild(buildTree(node.children));
				li.appendChild(collapseDiv);
			} else {
				const link = document.createElement('a');
				link.setAttribute('href', `/public/categories/${node.id}/products`);
				link.textContent = node.name;
				li.appendChild(link);
			}
			ul.appendChild(li);
		}
		return ul;
	}

	fetch(apiUrl)
		.then(res => {
			if (!res.ok) throw new Error('카테고리 데이터를 가져오지 못했습니다');
			return res.json();
		})
		.then(data => {
			container.innerHTML = '';
			const tree = buildTree(data);
			for (const child of Array.from(tree.children)) {
				container.appendChild(child);
			}
		})
		.catch(err => console.error(err));
});

// 헤더 네비게이션 바 active 클래스 토글.
document.addEventListener('DOMContentLoaded', () => {
    const navLinks = document.querySelectorAll('header nav a');
    if (!navLinks.length) return;

    const currentPath = window.location.pathname;
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        // 정확히 일치하거나, 하위 경로일 때도 active
        if (href === currentPath || currentPath.startsWith(href + '/')) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
});