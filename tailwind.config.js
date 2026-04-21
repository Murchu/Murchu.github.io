module.exports = {
  content: [
    './*.html',
    './src/**/*.{astro,html,js}'
  ],
  theme: {
    extend: {
      fontFamily: {
       MyFont: ['"Golos Text"', "sans-serif"],
      },
      backgroundImage: {
        'hero-pattern': "url('/src/img/pattern.svg')"
      }
    },
  }, 
  plugins: [
    require('@tailwindcss/typography')
  ]
}
