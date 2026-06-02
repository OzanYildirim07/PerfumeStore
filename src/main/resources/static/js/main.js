let cart = [];
let carouselPosition = 0;
const cartSidebar = document.getElementById('cartSidebar');
const cartOverlay = document.getElementById('cartOverlay');
const cartItemsContainer = document.getElementById('cartItems');
const cartTotalElement = document.getElementById('cartTotal');
const cartBadge = document.querySelector('.cart-badge');
const quickViewModal = document.getElementById('quickViewModal');
const toast = document.getElementById('toast');
const toastMessage = document.getElementById('toastMessage');

// ==========================================
// 🔐 GLOBAL AUTH STATE & FUNCTIONS (PREMIUM DROPDOWN AYARI)
// ==========================================
let currentUserToken = localStorage.getItem('token') || null;
let currentUsername = localStorage.getItem('username') || null;
let currentUserEmail = localStorage.getItem('userEmail') || ''; // Menüde göstermek için maili de tutuyoruz kanka

// Kullanıcı ikonuna basıldığında çalışan akıllı yönlendirici
function handleUserClick() {
    if (currentUserToken) {
        // Kullanıcı giriş yapmışsa oturumu kapatmak yerine mini pencereyi aç/kapat yapar kanka
        const dropdown = document.getElementById('profileDropdown');
        if (dropdown) {
            dropdown.classList.toggle('active');
        }
    } else {
        // Giriş yapmadıysa eskisi gibi giriş modalını tetikler
        toggleAuthModal();
    }
}

function toggleAuthModal() {
    const modal = document.getElementById('authModal');
    if (!modal) {
        console.error("Hata: HTML içinde id'si 'authModal' olan div bulunamadı!");
        return;
    }
    modal.classList.toggle('active');
    document.body.style.overflow = modal.classList.contains('active') ? 'hidden' : '';
}

function logoutUser() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('userEmail'); // Hafızayı tertemiz sıfırlıyoruz kanka
    currentUserToken = null;
    currentUsername = null;
    currentUserEmail = '';
    showToast('Oturum kapatıldı.');
    updateAuthUI();
    setTimeout(() => location.reload(), 1000);
}

function updateAuthUI() {
    const userBtnLabel = document.getElementById('userBtnLabel');
    const dropdownUsername = document.getElementById('dropdownUsername');
    const dropdownEmail = document.getElementById('dropdownEmail');
    
    const storedToken = localStorage.getItem('token');
    const storedUsername = localStorage.getItem('username');
    const storedEmail = localStorage.getItem('userEmail');

    if (storedToken && storedUsername) {
        if (userBtnLabel) userBtnLabel.textContent = storedUsername;
        // Mini profil panelinin içindeki yazıları milimetrik dolduruyoruz kanka
        if (dropdownUsername) dropdownUsername.textContent = storedUsername;
        if (dropdownEmail) dropdownEmail.textContent = storedEmail || 'E-posta tanımlanmadı';
    } else {
        if (userBtnLabel) userBtnLabel.textContent = '';
    }
}

// ==========================================
// 🛒 SEPET VE ARAYÜZ FONKSİYONLARI
// ==========================================
function toggleCart() {
    cartSidebar.classList.toggle('active');
    cartOverlay.classList.toggle('active');
    document.body.style.overflow = cartSidebar.classList.contains('active') ? 'hidden' : '';
}

// 🌟 PARAMETRE SIRASI: id, name, price, image (Kurşun Geçirmez Sıralama)
function addToCart(id, name, price, image = '') {
    const productId = Number(id);
    
    // 🚨 FORMATLI STRING VE SAF SAYI SAVAŞINI ÇÖZEN KALKAN KANKA
    let productPrice = price;
    if (typeof price === 'string') {
        // "₺" sembolünü, boşlukları ve sinsi karakterleri temizle
        let cleanPrice = price.replace('₺', '').trim();
        
        // Eğer fiyatta hem nokta hem virgül varsa (Örn: 7.200,00 -> 7200.00)
        if (cleanPrice.includes('.') && cleanPrice.includes(',')) {
            cleanPrice = cleanPrice.replace(/\./g, '').replace(',', '.');
        } 
        // Eğer sadece nokta varsa ve son iki haneden önceyse (Örn: 7.200 -> 7200) -> Binlik ayracıdır
        else if (cleanPrice.includes('.') && cleanPrice.split('.')[1].length === 3) {
            cleanPrice = cleanPrice.replace(/\./g, '');
        }
        
        productPrice = parseFloat(cleanPrice);
    } else {
        productPrice = parseFloat(price);
    }

    // Parse işlemi olur da NaN dönerse emniyet kemeri (Fiyat 0 kalmasın)
    if (isNaN(productPrice)) productPrice = 0;

    const existingItem = cart.find(item => Number(item.id) === productId);
    
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({
            id: productId, 
            name: name,
            price: productPrice, // Artık kuruşu kuruşuna taş gibi net sayı tutuyor!
            image: image || getDefaultImage(name),
            quantity: 1
        });
    }
    
    updateCartUI();
    showToast(`${name} sepete eklendi`);
    
    if (cartBadge) {
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        cartBadge.textContent = totalItems;
        cartBadge.style.display = totalItems > 0 ? 'flex' : 'none';
    }
}

function removeFromCart(id) {
    const productId = Number(id);
    const index = cart.findIndex(item => Number(item.id) === productId);
    
    if (index !== -1) {
        cart.splice(index, 1);
    }
    
    updateCartUI();
    
    if (cartBadge) {
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        cartBadge.textContent = totalItems || '0';
        if (!totalItems) cartBadge.style.display = 'none';
        else cartBadge.style.display = 'flex';
    }
}

function updateCartUI() {
    if (!cartItemsContainer) return;
    
    if (cart.length === 0) {
        cartItemsContainer.innerHTML = `
            <div class="cart-empty">
                <p style="text-align: center; color: var(--gray); padding: 60px 20px; font-size: 0.95rem;">
                    Sepetiniz boş
                </p>
            </div>
        `;
    } else {
        cartItemsContainer.innerHTML = cart.map((item) => `
            <div class="cart-item">
                <img src="${item.image}" alt="${item.name}" class="cart-item-image">
                <div class="cart-item-info">
                    <h4 class="cart-item-name">${item.name}</h4>
                    <p class="cart-item-price">${formatPrice(item.price)} ₺</p>
                    <p style="font-size: 0.8rem; color: var(--gray); margin-top: 4px;">Adet: ${item.quantity}</p>
                    <button class="cart-item-remove" onclick="removeFromCart(${item.id})">Kaldır</button>
                </div>
            </div>
        `).join('');
    }
    
    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    if (cartTotalElement) {
        cartTotalElement.textContent = formatPrice(total) + ' ₺';
    }
}

function getDefaultImage(name) {
    const imageMap = {
        'Golden Essence': 'https://images.unsplash.com/photo-1541643600914-78b084683601?w=200&q=80',
        'Midnight Orchid': 'https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?w=200&q=80',
        'Royal Musk': 'https://images.unsplash.com/photo-1587017539504-67cfbddac569?w=200&q=80',
        'Velvet Rose': 'https://images.unsplash.com/photo-1590736969955-71cc94901144?w=200&q=80',
        'Citrus Noir': 'https://images.unsplash.com/photo-1519669011783-4eaa95fa1b7d?w=200&q=80',
        'Amber Luxe': 'https://images.unsplash.com/photo-1595425970377-c9703cf48b6d?w=200&q=80'
    };
    return imageMap[name] || 'https://images.unsplash.com/photo-1594035910387-fea47794261f?w=200&q=80';
}

function formatPrice(price) {
    return price.toLocaleString('tr-TR');
}

// 🌟 QuickView Parametrelerine id ve volumeMl ekledik, başlığı 'İsim - 100ml' yaptık kanka
function openQuickView(id, name, price, image, description, volumeMl) {
    const modalImg = document.getElementById('modalImg');
    const modalTitle = document.getElementById('modalTitle');
    const modalPrice = document.getElementById('modalPrice');
    const modalAddBtn = document.getElementById('modalAddBtn');
    const modalDesc = document.getElementById('modalDesc');
    
    if (modalImg) modalImg.src = image;
    if (modalImg) modalImg.alt = name;
    if (modalTitle) modalTitle.textContent = volumeMl ? `${name} - ${volumeMl}ml` : name;
    if (modalPrice) modalPrice.textContent = price;
    if (modalDesc) modalDesc.textContent = description || '';
    
    const numericPrice = parseFloat(price.replace(/[^0-9.]/g, ''));
    if (modalAddBtn) {
        modalAddBtn.onclick = () => {
            addToCart(id, volumeMl ? `${name} (${volumeMl}ml)` : name, numericPrice, image);
            closeQuickView();
        };
    }
    
    if (quickViewModal) {
        quickViewModal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }
}

function closeQuickView() {
    if (quickViewModal) {
        quickViewModal.classList.remove('active');
        document.body.style.overflow = '';
    }
}

if (quickViewModal) {
    quickViewModal.addEventListener('click', (e) => {
        if (e.target === quickViewModal) {
            closeQuickView();
        }
    });
}

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        closeQuickView();
        if (cartSidebar.classList.contains('active')) {
            toggleCart();
        }
    }
});

function moveCarousel(direction) {
    const track = document.getElementById('carouselTrack');
    if (!track) return;
    
    const cards = track.querySelectorAll('.bestseller-card');
    if (cards.length === 0) return;
    
    const cardWidth = cards[0].offsetWidth + 25; // width + gap
    const containerWidth = track.parentElement.offsetWidth;
    const visibleCards = Math.floor(containerWidth / cardWidth);
    const maxPosition = Math.max(0, cards.length - visibleCards);
    
    carouselPosition += direction;
    
    if (carouselPosition < 0) carouselPosition = 0;
    if (carouselPosition > maxPosition) carouselPosition = maxPosition;
    
    const translateX = -(carouselPosition * cardWidth);
    track.style.transform = `translateX(${translateX}px)`;
}

let touchStartX = 0;
let touchEndX = 0;

const carouselWrapper = document.querySelector('.carousel-track-wrapper');
if (carouselWrapper) {
    carouselWrapper.addEventListener('touchstart', (e) => {
        touchStartX = e.changedTouches[0].screenX;
    }, { passive: true });
    
    carouselWrapper.addEventListener('touchend', (e) => {
        touchEndX = e.changedTouches[0].screenX;
        handleSwipe();
    }, { passive: true });
}

function handleSwipe() {
    const swipeThreshold = 50;
    const diff = touchStartX - touchEndX;
    
    if (Math.abs(diff) > swipeThreshold) {
        if (diff > 0) {
            moveCarousel(1); // Swipe left, move right
        } else {
            moveCarousel(-1); // Swipe right, move left
        }
    }
}

function toggleMenu() {
    const navMenu = document.querySelector('.nav-menu');
    if (navMenu) {
        navMenu.classList.toggle('active');
    }
}

const navLinks = document.querySelectorAll('.nav-link');
navLinks.forEach(link => {
    link.addEventListener('click', () => {
        const navMenu = document.querySelector('.nav-menu');
        if (navMenu) navMenu.classList.remove('active');
    });
});

function initScrollReveal() {
    const revealElements = document.querySelectorAll('.reveal-up, .reveal-left, .reveal-right');
    
    const observerOptions = {
        root: null,
        rootMargin: '0px',
        threshold: 0.15
    };
    
    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('revealed');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);
    
    revealElements.forEach(el => observer.observe(el));
}

function handleNavbarScroll() {
    const navbar = document.getElementById('navbar');
    if (!navbar) return;
    
    if (window.scrollY > 50) {
        navbar.classList.add('scrolled');
    } else {
        navbar.classList.remove('scrolled');
    }
}

let toastTimeout;

function showToast(message) {
    if (!toast || !toastMessage) return;
    
    toastMessage.textContent = message;
    toast.classList.add('active');
    
    clearTimeout(toastTimeout);
    toastTimeout = setTimeout(() => {
        toast.classList.remove('active');
    }, 3000);
}

/*function handleSubscribe(e) {
    e.preventDefault();
    const input = e.target.querySelector('.newsletter-input');
    if (input && input.value) {
        showToast('Bültenimize başarıyla abone oldunuz!');
        input.value = '';
    }
}*/

document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            const offset = 80; // navbar height
            const targetPosition = target.getBoundingClientRect().top + window.pageYOffset - offset;
            
            window.scrollTo({
                top: targetPosition,
                behavior: 'smooth'
            });
        }
    });
});

function initLazyLoad() {
    const images = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.removeAttribute('data-src');
                imageObserver.unobserve(img);
            }
        });
    });
    
    images.forEach(img => imageObserver.observe(img));
}

function initParallax() {
    const heroImg = document.querySelector('.hero-img');
    if (!heroImg) return;
    
    let ticking = false;
    
    window.addEventListener('scroll', () => {
        if (!ticking) {
            window.requestAnimationFrame(() => {
                const scrolled = window.pageYOffset;
                const rate = scrolled * 0.4;
                heroImg.style.transform = `translateY(${rate}px) scale(${1 + scrolled * 0.0002})`;
                ticking = false;
            });
            ticking = true;
        }
    });
}

function animateCounters() {
    const counters = document.querySelectorAll('.stat-number');
    
    const counterObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const counter = entry.target;
                const target = parseInt(counter.textContent);
                let current = 0;
                const increment = target / 60; // 60 frames
                const duration = 1500; // 1.5 seconds
                const stepTime = duration / 60;
                
                const timer = setInterval(() => {
                    current += increment;
                    if (current >= target) {
                        counter.textContent = target;
                        clearInterval(timer);
                    } else {
                        counter.textContent = Math.floor(current);
                    }
                }, stepTime);
                
                counterObserver.unobserve(counter);
            }
        });
    }, { threshold: 0.5 });
    
    counters.forEach(counter => counterObserver.observe(counter));
}

if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
    document.querySelectorAll('.reveal-up, .reveal-left, .reveal-right').forEach(el => {
        el.style.transition = 'none';
        el.classList.add('revealed');
    });
}

const API_BASE = '';

async function fetchPerfumes() {
    try {
        const response = await fetch(`${API_BASE}/api/perfumes`);
        if (!response.ok) throw new Error('API error');
        return await response.json();
    } catch (error) {
        console.error('Fetch error:', error);
        return [];
    }
}

async function searchPerfumes(query) {
    try {
        const response = await fetch(`${API_BASE}/api/perfumes/search?query=${encodeURIComponent(query)}`);
        if (!response.ok) throw new Error('API error');
        return await response.json();
    } catch (error) {
        console.error('Search error:', error);
        return [];
    }
}

async function filterByGender(gender) {
    try {
        const response = await fetch(`${API_BASE}/api/perfumes/gender/${gender}`);
        if (!response.ok) throw new Error('API error');
        return await response.json();
    } catch (error) {
        console.error('Filter error:', error);
        return [];
    }
}

function getGenderLabel(gender) {
    const map = { 'ERKEK': 'Erkek Parfüm', 'KADIN': 'Kadın Parfüm', 'UNISEX': 'Unisex Parfüm' };
    return map[gender] || 'Parfüm';
}

async function loadBestsellers() {
    const grid = document.getElementById('bestsellerGrid');
    if (grid) grid.innerHTML = '<div style="grid-column:1/-1;text-align:center;padding:60px;color:var(--gray);">Yükleniyor...</div>';
    
    try {
        const response = await fetch(`${API_BASE}/api/perfumes/bestsellers`);
        if (!response.ok) throw new Error('API hatası');
        
        const top3 = await response.json();
        renderBestsellers(top3);
    } catch (error) {
        console.error('Çok satanlar yüklenirken hata:', error);
        if (grid) grid.innerHTML = '<div style="grid-column:1/-1;text-align:center;padding:60px;color:var(--gray);">Sonuç bulunamadı</div>';
    }
}

function renderBestsellers(perfumes) {
    const grid = document.getElementById('bestsellerGrid');
    if (!grid) return;
    if (perfumes.length === 0) {
        grid.innerHTML = '<div style="grid-column:1/-1;text-align:center;padding:60px;color:var(--gray);">Sonuç bulunamadı</div>';
        return;
    }
    grid.innerHTML = perfumes.map((p, i) => {
        const notes = p.contents ? p.contents.join(', ') : '';
        const delay = i * 0.1;
        const safeName = p.name.replace(/'/g, "\\'");
        const safeDesc = p.description ? p.description.replace(/'/g, "\\'") : '';
        
        return `
        <div class="product-card reveal-up" style="transition-delay:${delay}s">
            <div class="product-image">
            <img src="${p.imageUrl}" alt="${p.name}" class="product-img" loading="lazy">
                <div class="product-overlay">
            <button class="btn-quickview" onclick="openQuickView(${p.id}, '${safeName}', '${formatPrice(p.price)} ₺', '${p.imageUrl}', '${safeDesc}', ${p.volumeMl})">İncele</button>
            </div>
        </div>
        <div class="product-info">
        <span class="product-category">${getGenderLabel(p.gender)} - ${p.brand} - ${p.volumeMl}ML</span>
        <h3 class="product-name">${p.name}</h3>
                <p class="product-notes">${notes}</p>
                <p class="product-price">${formatPrice(p.price)} ₺</p>
                <button class="btn btn-small" onclick="addToCart(${p.id}, '${safeName} (${p.volumeMl}ml)', ${p.price}, '${p.imageUrl}')">Sepete Ekle</button>
            </div>
        </div>`;
    }).join('');
    initScrollReveal();
}

function renderPerfumes(perfumes) {
    const grid = document.getElementById('collectionGrid');
    if (!grid) return;
    if (perfumes.length === 0) {
        grid.innerHTML = '<div style="grid-column:1/-1;text-align:center;padding:60px;color:var(--gray);">Sonuç bulunamadı</div>';
        return;
    }
    grid.innerHTML = perfumes.map((p, i) => {
        const notes = p.contents ? p.contents.join(', ') : '';
        const delay = (i % 6) * 0.1;
        const safeName = p.name.replace(/'/g, "\\'");
        const safeDesc = p.description ? p.description.replace(/'/g, "\\'") : '';
        
        return `
        <div class="product-card reveal-up" style="transition-delay:${delay}s">
    <div class="product-image">
        <img src="${p.imageUrl}" alt="${p.name}" class="product-img" loading="lazy">
        <div class="product-overlay">
            <button class="btn-quickview" onclick="openQuickView(${p.id}, '${safeName}', '${formatPrice(p.price)} ₺', '${p.imageUrl}', '${safeDesc}', ${p.volumeMl})">İncele</button>
        </div>
    </div>
    <div class="product-info">
        <span class="product-category">${getGenderLabel(p.gender)} - ${p.brand} - ${p.volumeMl}ML</span>
        <h3 class="product-name">${p.name}</h3>
                <p class="product-notes">${notes}</p>
                <p class="product-price">${formatPrice(p.price)} ₺</p>
                <button class="btn btn-small" onclick="addToCart(${p.id}, '${safeName} (${p.volumeMl}ml)', ${p.price}, '${p.imageUrl}')">Sepete Ekle</button>
            </div>
        </div>`;
    }).join('');
    initScrollReveal();
}

async function loadPerfumes(gender) {
    const grid = document.getElementById('collectionGrid');
    if (grid) grid.innerHTML = '<div style="grid-column:1/-1;text-align:center;padding:60px;color:var(--gray);">Yükleniyor...</div>';
    let data;
    if (gender && gender !== 'all') {
        data = await filterByGender(gender);
    } else {
        data = await fetchPerfumes();
    }
    renderPerfumes(data);
}

function doSearch() {
    const input = document.getElementById('searchInput');
    if (!input || !input.value.trim()) {
        loadPerfumes('all');
        return;
    }
    const grid = document.getElementById('collectionGrid');
    if (grid) grid.innerHTML = '<div style="grid-column:1/-1;text-align:center;padding:60px;color:var(--gray);">Aranıyor...</div>';
    
    searchPerfumes(input.value.trim()).then(data => {
        renderPerfumes(data);
        if (grid) {
            grid.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    });
}

// ==========================================
// 💳 ÖDEME EKRANI MODAL MANTIĞI & SİPARİŞ ENTEGRASYONU (GLOBAL ALAN)
// ==========================================
let selectedPaymentMethod = 'CREDIT_CARD'; // Varsayılan değer kanka

function openPaymentModal() {
    // 1. Giriş kontrolü kanka
    const token = localStorage.getItem('token');
    if (!token) {
        showToast("Alışverişi Tamamlamak İçin Lütfen Önce Giriş Yapın!");
        if (cartSidebar) cartSidebar.classList.remove('active');
        if (cartOverlay) cartOverlay.classList.remove('active');
        toggleAuthModal();
        return;
    }

    // 2. Sepet kontrolü
    if (cart.length === 0) {
        showToast("Sepetiniz Boş.Lütfen Sepetinize Ürün Ekleyiniz!");
        return;
    }

    // Sepeti kapatıp ödeme ekranını açıyoruz
    if (cartSidebar) cartSidebar.classList.remove('active');
    if (cartOverlay) cartOverlay.classList.remove('active');

    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const modalTotalTxt = document.getElementById('paymentModalTotal');
    if (modalTotalTxt) modalTotalTxt.textContent = `Toplam Tutar: ${formatPrice(total)} ₺`;

    const payModal = document.getElementById('paymentModal');
    if (payModal) {
        payModal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    // Yöntem seçim butonlarını dinlemeye alıyoruz kanka
    const methodBtns = document.querySelectorAll('.payment-method-btn');
    methodBtns.forEach(btn => {
        btn.onclick = () => {
            methodBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            selectedPaymentMethod = btn.dataset.method; // BE'nin beklediği enum ismi (CREDIT_CARD vs)
        };
    });
}

function closePaymentModal() {
    const payModal = document.getElementById('paymentModal');
    if (payModal) {
        payModal.classList.remove('active');
        document.body.style.overflow = '';
    }
}

// 🔥 BACKEND ŞABLONUYLA %100 UYUMLU SİPARİŞİ UÇURAN SİHİRLİ FONKSİYON
async function submitOrderWithPayment() {
    const token = localStorage.getItem('token');
    const BACKEND_URL = API_BASE || 'http://localhost:8080';

    // 1. Backend'in tam olarak beklediği kurşun geçirmez şablon kanka!
    const payload = {
       paymentMethod: selectedPaymentMethod, // 🔥 Ana gövdede beklenen metot (CREDIT_CARD vs.)
       items: cart.map(item => ({
            perfumeId: Number(item.id), // 🔥 Sepetteki parfümün ID'si
            quantity: item.quantity
       }))
    };

    try {
        const response = await fetch(`${BACKEND_URL}/api/orders`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` // Güvenlik duvarını aşmak için şart
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "Sipariş işlenirken bir hata oluştu.");
        }

        // Başarılı tost mesajı şovu!
        showToast(`🎉 Sipariş ve Ödeme Başarılı!`);
        
        // Sepeti tamamen sıfırla kanka
        cart = [];
        updateCartUI();
        if (cartBadge) {
            cartBadge.textContent = '0';
            cartBadge.style.display = 'none';
        }
        
        closePaymentModal();

    } catch (error) {
        showToast(error.message);
    }
}

// ==========================================
// 🚀 DOM CONTENT LOADED - EVENT LISTENERS
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    // 🚨 SİTEYE GİRER GİRMEZ TOKEN SÜRESİ KONTROLÜ (AUTO-LOGOUT) KANKA
    const savedToken = localStorage.getItem('token');
    if (savedToken) {
        try {
            // JWT token'ın içindeki payload kısmını çözüyoruz
            const payload = JSON.parse(atob(savedToken.split('.')[1]));
            const currentTime = Math.floor(Date.now() / 1000); // Şu anki zamanı saniye cinsinden alıyoruz
            
            // Eğer token'ın bitiş süresi (exp) şu anki zamandan küçükse süre dolmuştur kanka
            if (payload.exp && payload.exp < currentTime) {
                console.log("Token süresi dolmuş, otomatik çıkış yapılıyor...");
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                localStorage.removeItem('userEmail');
                currentUserToken = null;
                currentUsername = null;
                currentUserEmail = '';
            }
        } catch (e) {
            console.error("Token decode edilirken hata oluştu, temizleniyor:", e);
            localStorage.clear();
        }
    }

    initScrollReveal();
    initLazyLoad();
    initParallax();
    animateCounters();
    window.addEventListener('scroll', handleNavbarScroll);
    handleNavbarScroll();
    updateCartUI();
    updateAuthUI();

    loadPerfumes('all');
    loadBestsellers();

    const searchBtn = document.querySelector('.search-btn');
    if (searchBtn) {
        searchBtn.addEventListener('click', (e) => {
            e.stopPropagation(); 
            const existing = document.getElementById('searchBar');
            if (existing) { existing.remove(); return; }
            
            const bar = document.createElement('div');
            bar.id = 'searchBar';
            bar.innerHTML = `
                <div id="searchInnerContainer" style="position:fixed;top:80px;left:50%;transform:translateX(-50%);z-index:999;background:#fff;padding:16px 24px;border-radius:8px;box-shadow:0 8px 32px rgba(0,0,0,0.15);display:flex;gap:12px;max-width:500px;width:90%;border:1px solid rgba(212,175,55,0.2);">
                    <input type="text" id="searchInput" placeholder="Parfüm adı, nota veya marka ara..." style="flex:1;padding:12px 16px;border:1px solid #ddd;border-radius:6px;font-size:14px;outline:none;font-family:var(--font-body);">
                    <button onclick="doSearch()" style="padding:12px 24px;background:var(--gold);color:#000;border:none;border-radius:6px;font-weight:600;cursor:pointer;font-family:var(--font-body);">Ara</button>
                </div>`;
            document.body.appendChild(bar);
            
            setTimeout(() => document.getElementById('searchInput')?.focus(), 100);
            document.getElementById('searchInput')?.addEventListener('keypress', (e) => { if (e.key === 'Enter') doSearch(); });
        });
    }

    // 🛠️ Ortak tıklama dinleyicisi: Arama kutusu ve Profil menüsünü dışına tıklanınca kapatır
    document.addEventListener('click', (e) => {
        // Arama kapatma logic'i
        const searchBar = document.getElementById('searchBar');
        const searchInner = document.getElementById('searchInnerContainer');
        if (searchBar && searchInner && !searchInner.contains(e.target)) {
            searchBar.remove();
        }

        // Boşluğa tıklayınca profil dropdown menüsünü kapatma logic'i
        const profileDropdown = document.getElementById('profileDropdown');
        if (profileDropdown && !profileDropdown.contains(e.target) && !e.target.closest('.user-btn')) {
            profileDropdown.classList.remove('active');
        }
        
        // Ödeme modalının dışına tıklayınca kapanma logic'i kanka
        const payModal = document.getElementById('paymentModal');
        if (payModal && e.target === payModal) {
            closePaymentModal();
        }
    });

    // 🔐 Auth DOM Elements & Tab Switching
    const authTabs = document.querySelectorAll('.auth-tab');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    authTabs.forEach(tab => {
        tab.addEventListener('click', () => {
            authTabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            
            const mode = tab.dataset.mode;
            if (mode === 'login') {
                if (loginForm) loginForm.style.display = 'block';
                if (registerForm) registerForm.style.display = 'none';
            } else {
                if (loginForm) loginForm.style.display = 'none';
                if (registerForm) registerForm.style.display = 'block';
            }
        });
    });

    // BACKEND LOGIN SUBMIT
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const emailOrUsername = document.getElementById('loginEmail')?.value;
            const password = document.getElementById('loginPassword')?.value;

            const BACKEND_URL = API_BASE || 'http://localhost:8080';

            try {
                const response = await fetch(`${BACKEND_URL}/api/auth/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ 
                        username: emailOrUsername,
                        password: password 
                    })
                });

                if (!response.ok) throw new Error('E-posta/Kullanıcı adı veya şifre hatalı!');
                const data = await response.json();

                console.log("Giriş Başarılı! Backend'den Dönen Veri:", data);

                const token = data.token || data.accessToken || data.jwt || data.tokenString;
                const username = data.username || data.name || data.user?.username || emailOrUsername;
                const email = data.email || data.user?.email || (emailOrUsername.includes('@') ? emailOrUsername : 'E-posta tanımlanmadı');

                if (token) {
                    localStorage.setItem('token', token);
                    localStorage.setItem('username', username);
                    localStorage.setItem('userEmail', email);
                    
                    showToast('Başarıyla giriş yapıldı!');
                    setTimeout(() => {
                        location.reload();
                    }, 1000);
                } else {
                    throw new Error("Giriş başarılı ama token alınamadı.");
                }

            } catch (error) {
                showToast(error.message);
            }
        });
    }

    // BACKEND REGISTER SUBMIT
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = document.getElementById('regName')?.value;
            const email = document.getElementById('regEmail')?.value;
            const password = document.getElementById('regPassword')?.value;

            try {
                const response = await fetch(`${API_BASE}/api/auth/register`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, email, password })
                });

                if (!response.ok) throw new Error('Kayıt oluşturulamadı, bilgilerini kontrol et!');
                
                showToast('Kayıt başarılı! Şimdi giriş yapabilirsiniz.');
                if (authTabs[0]) authTabs[0].click();
            } catch (error) {
                showToast(error.message);
            }
        });
    }

    // Modal click-outside close logic
    const authModalElement = document.getElementById('authModal');
    if (authModalElement) {
        authModalElement.addEventListener('click', (e) => {
            if (e.target === authModalElement) toggleAuthModal();
        });
    }
});