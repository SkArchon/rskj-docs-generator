module.exports = {
  preset: '@vue/cli-plugin-unit-jest/presets/typescript-and-babel',
  setupFiles: ['jest-localstorage-mock'],
  // We generate all the components dynamically, so we only include one of each type
  collectCoverageFrom: ['src/App.vue', 'src/components/BlockResultDTOModel.vue', 'src/components/EthgetBlockByNumber.vue']
}
