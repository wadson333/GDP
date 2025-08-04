/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}', './src/main/webapp/**/*.{html,ts}', './node_modules/primeng/**/*.{js,ts}'],
  theme: {
    extend: {
      // Vous pouvez étendre avec les couleurs de vos thèmes PrimeNG
      // colors: {
      //   'prime-blue': '#3B82F6',
      //   'prime-indigo': '#6366F1',
      //   'prime-purple': '#8B5CF6',
      // },
    },
  },
  plugins: [],
  // Configuration importante pour éviter les conflits
  important: true,
  corePlugins: {
    preflight: false, // Très important : désactive le reset CSS de Tailwind
  },
};
