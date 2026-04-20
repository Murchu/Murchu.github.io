class SiteHeader extends HTMLElement {
  constructor() {
    super();
  }

  connectedCallback() {
    const gradientClass = this.getAttribute('gradient') || '';
    const isNone = !gradientClass || gradientClass === 'none';

    // Determine colors/logos based on theme
    const theme = {
      navClass: isNone ? "text-slate-600" : "text-slate-200",
      hoverClass: isNone ? "hover:text-teal-700" : "hover:text-white",
      logoMobile: isNone ? "src/img/logos/SMlogo.svg" : "src/img/logos/SMlogoAlt.svg",
      logoDesktop: isNone ? "src/img/logos/logo.svg" : "src/img/logos/logoAlt.svg",
      bgWrapper: isNone ? "" : `${gradientClass} z-10`
    };

    this.innerHTML = `
      <div class="${theme.bgWrapper}">
        <nav class="flex justify-between px-6 py-8 mx-auto sm:max-w-screen-xl sm:mx-auto bg-transparent">
          <a href="https://www.iamcolm.com/" aria-label="Home">
            <div class="sm:hidden">
              <img class="h-6" src="${theme.logoMobile}" alt="logo" />
            </div>
            <div class="hidden sm:block">
              <img class="h-6" src="${theme.logoDesktop}" alt="logo" />
            </div>
          </a>

          <div class="hidden font-normal space-x-8 lg:flex ${theme.navClass}">
            <a class="${theme.hoverClass}" href="index.html">Home</a>
            <a class="${theme.hoverClass}" href="src/Colm_Murphy_CV.pdf">About</a>
            <a class="${theme.hoverClass}" href="process.html">Process</a>
          </div>

          <div class="flex lg:hidden z-20">
            <div class="relative inline-block text-left dropdown">
              <button 
                id="menu-button"
                type="button" 
                class="inline-flex justify-center w-full px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm hover:bg-gray-50 focus:outline-none" 
                aria-haspopup="true" 
                aria-expanded="false">
                <span>More</span>
                <svg class="w-5 h-5 ml-2 -mr-1" viewBox="0 0 20 20" fill="currentColor">
                  <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"></path>
                </svg>
              </button>

              <div class="opacity-0 invisible dropdown-menu transition-all duration-300 absolute right-0 w-56 mt-2 origin-top-right bg-white border border-gray-200 divide-y divide-gray-100 rounded-md shadow-lg outline-none" role="menu">
                <a href="index.html" class="block px-4 py-3 text-sm text-gray-700 hover:bg-gray-100" role="menuitem">Home</a>
                <a href="src/Colm_Murphy_CV.pdf" class="block px-4 py-3 text-sm text-gray-700 hover:bg-gray-100" role="menuitem">About</a>
                <a href="process.html" class="block px-4 py-3 text-sm text-gray-700 hover:bg-gray-100" role="menuitem">Process</a>
              </div>
            </div>
          </div>
        </nav>
      </div>
    `;

    this.setupEventListeners();
  }

  setupEventListeners() {
    const btn = this.querySelector('#menu-button');
    if (btn) {
      // Toggle aria-expanded for accessibility when hovering/focusing
      btn.addEventListener('mouseenter', () => btn.setAttribute('aria-expanded', 'true'));
      btn.parentElement.addEventListener('mouseleave', () => btn.setAttribute('aria-expanded', 'false'));
    }
  }
}

customElements.define('site-header', SiteHeader);