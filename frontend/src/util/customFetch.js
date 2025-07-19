import { getAccessToken } from '@/auth/accessToken';
import axios from 'axios';
import { renewAccessToken } from '@/auth/renewAccessToken';

const RETRY_SYMBOL = Symbol('retry');

const instance = axios.create({
    baseURL: '/api'
});

instance.interceptors.request.use(
  config => {
    const token = getAccessToken();
    if (token) {
      config.headers = config.headers || {};
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

instance.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (error.status === 401 && !originalRequest[RETRY_SYMBOL]) {
      originalRequest[RETRY_SYMBOL] = true;
      try {
        await renewAccessToken();
        return instance(originalRequest);
      } catch (err) {
        return Promise.reject(error);
      }
    }

    return Promise.reject(error);
  }
);

/**
 * Axios 인스턴스를 이용한 커스텀 Fetch 함수
 *
 * @template T
 * @param {Object} endpoint - 요청할 엔드포인트 정보
 * @param {import('axios').Method} endpoint.method - HTTP 메서드 (e.g. 'get', 'post')
 * @param {string} endpoint.url - 요청할 URL
 * @param {import('axios').AxiosRequestConfig} [config] - 추가 Axios 요청 설정
 * @returns {Promise<{
 *   data: T;
 *   headers: import('axios').AxiosResponseHeaders;
 *   status: number;
 * }>} 응답 데이터, 헤더, 상태코드를 포함한 객체
 */
export async function customFetch(endpoint, config) {
    const response = await instance({
        ...endpoint,
        ...config
    });

    return {
        data: response.data,
        headers: response.headers,
        status: response.status
    };
}
