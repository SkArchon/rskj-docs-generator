jest.mock('axios');

import { mount, shallowMount } from '@vue/test-utils'
import EthgetBlockByNumber from '@/components/EthgetBlockByNumber.vue'
import axios from 'axios';
import { SUCCESSFUL_METHOD_WITH_VALID_SCHEMA, SUCCESSFUL_TRY_REQUEST } from './data';
import Vue from 'vue';

it('expand the entry', () => {
  const wrapper = mount(EthgetBlockByNumber)
  const vm = (wrapper.vm as any);

  vm.toggleExpand();
  const newExpanded = vm.expanded;

  expect(newExpanded).toBe(true);
})

describe('switch to run mode', () => {
  it('first time', () => {
    const wrapper = mount(EthgetBlockByNumber)
    const vm = (wrapper.vm as any);

    const currentTryRequest = vm.tryRequest;

    vm.switchRunMode();

    expect(vm.runMode).toBe(true);
    expect(currentTryRequest).not.toBe(vm.tryRequest);
    expect(Object.keys(JSON.parse(vm.tryRequest))).toContain("jsonrpc");
  })

  it('after first time', () => {
    const wrapper = mount(EthgetBlockByNumber)
    const vm = (wrapper.vm as any);

    // Toggle run mode on and off
    vm.switchRunMode();
    vm.switchRunMode();

    const currentTryRequest = vm.tryRequest;

    vm.switchRunMode();

    expect(vm.runMode).toBe(true);
    expect(currentTryRequest).toBe(vm.tryRequest);
  })
})

it('reset try request', () => {
  const wrapper = mount(EthgetBlockByNumber)
  const vm = (wrapper.vm as any);

  const tryRequest = { abcd: 'testing' };
  vm.tryRequest = tryRequest;

  vm.resetTryRequest();

  expect(vm.tryRequest).not.toBe(tryRequest);
  expect(Object.keys(JSON.parse(vm.tryRequest))).toContain("jsonrpc");
})

it('reset errors', () => {
  const wrapper = mount(EthgetBlockByNumber)
  const vm = (wrapper.vm as any);

  vm.requestValidationFailed = true;
  vm.requestValidationErrors = {};
  vm.responseValidationFailed = true;
  vm.responseValidationErrors = {};

  vm.clearValidationErrors();

  expect(vm.requestValidationFailed).toBe(false);
  expect(vm.requestValidationErrors).toBe(null);
  expect(vm.responseValidationFailed).toBe(false);
  expect(vm.responseValidationErrors).toBe(null);
})

describe('run read method', () => {

  it('with invalid requet for request schema', async () => {
    const wrapper = mount(EthgetBlockByNumber)
    const vm = (wrapper.vm as any);
  
    vm.methodType = 'READ';
  
    vm.tryRequest = JSON.stringify({ value: 'dataset' });

    const response = SUCCESSFUL_METHOD_WITH_VALID_SCHEMA;
    (axios.post as any).mockImplementation(() => Promise.resolve(response))
  
    vm.runCall();
  
    await Vue.nextTick();
  
    expect(vm.requestValidationFailed).toBe(true);
    expect(JSON.parse(vm.requestValidationErrors).length).toBeGreaterThan(0);
  })

  it('with invalid response for response schema', async () => {
    const wrapper = mount(EthgetBlockByNumber)
    const vm = (wrapper.vm as any);
  
    vm.methodType = 'READ';
  
    vm.tryRequest = JSON.stringify(SUCCESSFUL_TRY_REQUEST);

    const response = { data: {}, status: 'Successful' };
    (axios.post as any).mockImplementation(() => Promise.resolve(response))
  
    vm.runCall();
  
    await Vue.nextTick();
  
    expect(vm.responseValidationFailed).toBe(true);
    expect(JSON.parse(vm.responseValidationErrors).length).toBeGreaterThan(0);
  })

  it('with successful request and response schema validation', async () => {
    const wrapper = mount(EthgetBlockByNumber)
    const vm = (wrapper.vm as any);
  
    vm.methodType = 'READ';
  
    const response = SUCCESSFUL_METHOD_WITH_VALID_SCHEMA;
  
    (axios.post as any).mockImplementation(() => Promise.resolve(response))
  
    vm.tryRequest = JSON.stringify(SUCCESSFUL_TRY_REQUEST);
    vm.runCall();
  
    const expectedResponseResult = {
      statusText: 'Success',
      status: 200,
      errorCode: null,
      ok: true,
    };
  
    await Vue.nextTick();
  
    expect(vm.responseResult).toStrictEqual(expectedResponseResult);
    expect(vm.requestPending).toBe(false);
    expect(vm.requestValidationFailed).toBe(false);
    expect(vm.requestValidationErrors).toBe('[]');
    expect(vm.responseValidationFailed).toBe(false);
    expect(vm.responseValidationErrors).toBe('[]');
  })

})

describe('run read transaction', () => {

  it('without a wallet connected', () => {
    const wrapper = mount(EthgetBlockByNumber)
    const vm = (wrapper.vm as any);
    vm.methodType = 'WRITE';

    window.alert = () => {};

    const responseResult = 'Test';
    vm.responseResult = responseResult;

    vm.runCall();

    expect(vm.responseResult).toBe(responseResult);
  })

  describe('with a wallet connected', () => {

    it('request validation failed wtih request schema', () => {
      const wrapper = mount(EthgetBlockByNumber)
      const vm = (wrapper.vm as any);
      vm.methodType = 'WRITE';
  
      vm.tryRequest = JSON.stringify({ value: 'dataset' });
      vm.runCall();
  
      expect(vm.requestValidationFailed).toBe(true);
      expect(JSON.parse(vm.requestValidationErrors).length).toBeGreaterThan(0);
    })

    it('successful request', async () => {
      const wrapper = mount(EthgetBlockByNumber)
      const vm = (wrapper.vm as any);
      vm.methodType = 'WRITE';
      const transactionHash = '0x6876788';

      Vue.prototype.$web3Result = {
        request: (value) => {
          return Promise.resolve(transactionHash);
        }
      }
      vm.tryRequest = JSON.stringify(SUCCESSFUL_TRY_REQUEST);

      vm.runCall();
  
      await Vue.nextTick();

      const expectedResult = { ok: true, statusText: 'Success' };
      expect(vm.responseResult).toStrictEqual(expectedResult);
      expect(vm.sentResponseCode).toBe(transactionHash);
      expect(vm.requestPending).toBe(false);
      expect(vm.requestValidationFailed).toBe(false);
      expect(vm.requestValidationErrors).toBe('[]');
    })

    it('request failed then', async () => {
      const wrapper = mount(EthgetBlockByNumber)
      const vm = (wrapper.vm as any);
      vm.methodType = 'WRITE';

      const errorCode = 'errorCode';
      const errorResponse = { code: errorCode, details: 'somedetails' };
      Vue.prototype.$web3Result = {
        request: () => {
          return Promise.reject(errorResponse);
        }
      }
      vm.tryRequest = JSON.stringify(SUCCESSFUL_TRY_REQUEST);

      vm.runCall();
  
      await Vue.nextTick();

      const expectedResult = { errorCode : errorCode, ok: false };
      expect(vm.responseResult).toStrictEqual(expectedResult);
      expect(JSON.parse(vm.sentResponseCode)).toStrictEqual(errorResponse);
      expect(vm.requestPending).toBe(false);
    })

  });


})

