import axios from "axios";
import { getRefreshToken } from "./refreshToken";
import { setAccessToken } from "./accessToken";
import { ENDPOINTS } from "@/util/endpoints";

const instance = axios.create({
  baseURL: '/api'
});

async function renewAccessTokenInternal() {
  const refreshToken = getRefreshToken();
  if (!refreshToken) {
    throw new Error("토큰이 없습니다.");
  }
  const response = await instance({
    ...ENDPOINTS.auth.refreshToken,
    data: { refreshToken: getRefreshToken() }
  });
  setAccessToken(response.data.accessToken);
}

let isRefreshing = false;
let refreshPromise = null;

export async function renewAccessToken() {
  if (isRefreshing) {
    return refreshPromise;
  }

  isRefreshing = true;
  refreshPromise = renewAccessTokenInternal();
  try {
    await refreshPromise;
  } finally {
    isRefreshing = false;
    refreshPromise = null;
  }
}
