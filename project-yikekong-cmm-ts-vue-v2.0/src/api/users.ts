import request from '@/utils/request'

export const getUserInfo = (data: any) =>
  request({
    url: '/api/users/info',
    method: 'post',
    data
  })

export const login = (data: any) =>
  request({
    url: '/api/login',
    method: 'post',
    data
  })

export const logout = () =>
  request({
    url: '/api/users/logout',
    method: 'post'
  })
