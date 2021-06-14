jest.mock('axios');

import { mount, shallowMount } from '@vue/test-utils'
import BlockResultDTOModel from '@/components/BlockResultDTOModel.vue'
import EthgetBlockByNumber from '@/components/EthgetBlockByNumber.vue'
import Vue from 'vue';

it('expand the entry', () => {
  const wrapper = mount(BlockResultDTOModel)
  const vm = (wrapper.vm as any);

  vm.toggleExpand();
  const newExpanded = vm.expanded;

  expect(newExpanded).toBe(true);
})

it('navigate to model from method', async () => {
  const methodWrapper = mount(EthgetBlockByNumber)
  const methodVm = (methodWrapper.vm as any);

  const modelWrapper = mount(BlockResultDTOModel)
  const modelVm = (modelWrapper.vm as any);

  expect(modelVm.expanded).toBe(false);

  methodVm.navigateToModelResponseBlockResultDTO();

  await Vue.nextTick();

  expect(modelVm.expanded).toBe(true);
})