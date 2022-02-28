module.exports = {
  content: [
    './*.html'
  ],
  theme: {
    extend: {
      fontFamily: {
       MyFont: ["Open Sans", "sans-serif"],
      },
    },
  }, 
  plugins: [
    require('@tailwindcss/typography')
  ]
}
