jest.mock('axios');

import { mount, shallowMount } from '@vue/test-utils'
import App from '@/App.vue'
import axios from 'axios';
import { SUCCESSFUL_METHOD_WITH_VALID_SCHEMA, SUCCESSFUL_TRY_REQUEST } from './data';
import Vue from 'vue';

declare var global: any;

beforeEach(() => {
  // to fully reset the state between tests, clear the storage
  localStorage.clear();
  // and reset all mocks
  jest.clearAllMocks();

  global.ethereum = undefined;
  
  // clearAllMocks will impact your other mocks too, so you can optionally reset individual mocks instead:
  (localStorage.setItem as any).mockClear();
});

it('expand default url to be set when not cached already', () => {
  const wrapper = mount(App)

  const defaultNetworkUrl = "https://public-node.testnet.rsk.co";
  expect(Vue.prototype.$readHostUrl).toBe(defaultNetworkUrl);
  expect(localStorage.__STORE__["networkUrl"]).toBe(defaultNetworkUrl);
})

it('expand default url to be set from cache when cached', () => {
  const networkUrl = "https://public-node.rsk.co";
  localStorage.setItem("networkUrl", networkUrl);
  localStorage.__STORE__["networkUrl"] = networkUrl;
  
  const wrapper = mount(App)
  const vm = (wrapper.vm as any);

  expect(Vue.prototype.$readHostUrl).toBe(networkUrl);
  expect(localStorage.__STORE__["networkUrl"]).toBe(networkUrl);
  expect(vm.details.networkUrlOnSelect).toBe(networkUrl);
})

it('expand default status set when wallet is not connected', () => { 
  const wrapper = mount(App)
  const vm = (wrapper.vm as any);

  expect(localStorage.__STORE__["walletConnectedStatus"]).toBe('NOT_CONNECTED');
  expect(vm.walletConnectedStatus).toBe('NOT_CONNECTED');
})

it('when connected previously but wallet was uninstalled', () => {
  localStorage.setItem("walletConnectedStatus", 'CONNECTED');

  const wrapper = mount(App)
  const vm = (wrapper.vm as any);

  expect(localStorage.__STORE__["walletConnectedStatus"]).toBe('NOT_CONNECTED');
  expect(vm.walletConnectedStatus).toBe('NOT_CONNECTED');
})

it('when connected previously reconnect unsuccessfully', async () => {
  localStorage.setItem("walletConnectedStatus", 'CONNECTED');

  global.ethereum = {
    request: (currValue) => {
      return Promise.reject("value");
    }
  }

  const wrapper = mount(App)
  const vm = (wrapper.vm as any);

  await Vue.nextTick();

  expect(Vue.prototype.$web3Result).toBe(null);
  expect(localStorage.__STORE__["walletConnectedStatus"]).toBe('NOT_CONNECTED');
})

it('when connected previously reconnect successfully', async () => {
  localStorage.setItem("walletConnectedStatus", 'CONNECTED');

  global.ethereum = {
    request: (currValue) => {
      return Promise.resolve("value");
    }
  }

  const wrapper = mount(App)
  const vm = (wrapper.vm as any);

  await Vue.nextTick();

  expect(localStorage.__STORE__["walletConnectedStatus"]).toBe('CONNECTED');
  expect(Vue.prototype.$web3Result).toBe(global.ethereum);
})


describe('update network selection', () => {
  
  it('change to custom', () => {
    const wrapper = mount(App)
    const vm = (wrapper.vm as any);

    const event = {
      target: {
        value: 'custom'
      }
    };
    vm.changeNetworkSelect(event);

    expect(vm.enableCustom).toBe(true);
    expect(vm.details.networkUrl).toBe("");
    expect(localStorage.__STORE__["networkUrl"]).toBe("");
    expect(Vue.prototype.$readHostUrl).toBe("");
  })

  it('change to testnet', () => {
    const wrapper = mount(App)
    const vm = (wrapper.vm as any);

    const testnetUrl = 'https://public-node.testnet.rsk.co';
    const event = {
      target: {
        value: testnetUrl
      }
    };
    vm.changeNetworkSelect(event);

    expect(vm.enableCustom).toBe(false);
    expect(vm.details.networkUrl).toBe(testnetUrl);
    expect(localStorage.__STORE__["networkUrl"]).toBe(testnetUrl);
    expect(Vue.prototype.$readHostUrl).toBe(testnetUrl);
  })
})

it('network custom text updated', () => {
  const wrapper = mount(App)
  const vm = (wrapper.vm as any);

  const selectEvent = {
    target: {
      value: 'custom'
    }
  };
  vm.changeNetworkSelect(selectEvent);

  const customUrl = 'http://someurl';
  const textEvent = {
    target: {
      value: customUrl
    }
  };
  vm.networkTextChanged(textEvent);

  expect(vm.details.networkUrl).toBe(customUrl);
  expect(localStorage.__STORE__["networkUrl"]).toBe(customUrl);
  expect(Vue.prototype.$readHostUrl).toBe(customUrl);
})

describe('connect wallet', () => {

  it('when a wallet is not present', async () => {
    const wrapper = mount(App)
    const vm = (wrapper.vm as any);

    window.alert = () => {};

    vm.connectWallet();

    await Vue.nextTick();

    const expectedStatus = 'NOT_CONNECTED';
    expect(localStorage.__STORE__["walletConnectedStatus"]).toBe(expectedStatus);
    expect(vm.walletConnectedStatus).toBe(expectedStatus);
  })

  it('when a wallet is not present and connection unsuccessful', async () => {
    const wrapper = mount(App)
    const vm = (wrapper.vm as any);

    global.ethereum = {
      request: (currValue) => {
        return Promise.reject("value");
      }
    }

    vm.connectWallet();

    await Vue.nextTick();

    const expectedStatus = 'NOT_CONNECTED';
    expect(localStorage.__STORE__["walletConnectedStatus"]).toBe(expectedStatus);
    expect(vm.walletConnectedStatus).toBe(expectedStatus);
  })

  it('when a wallet is not present and successfully connected', async () => {
    const wrapper = mount(App)
    const vm = (wrapper.vm as any);

    global.ethereum = {
      request: (currValue) => {
        return Promise.resolve("value");
      }
    }

    vm.connectWallet();

    await Vue.nextTick();

    const expectedStatus = 'CONNECTED';
    expect(localStorage.__STORE__["walletConnectedStatus"]).toBe(expectedStatus);
    expect(vm.walletConnectedStatus).toBe(expectedStatus);
    expect(Vue.prototype.$web3Result).toBe(global.ethereum);
  })

})

