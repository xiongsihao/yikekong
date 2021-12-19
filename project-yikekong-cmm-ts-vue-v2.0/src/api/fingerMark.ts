import request from '@/utils/request'

export const quota = (params: any) =>
    request({
        url: '/api/quota',
        method: 'get',
        params
    })
export const addQuota = (data: any) =>
    request({
        url: '/api/quota',
        method: 'post',
        data
    })
export const putQuota = (data: any) =>
    request({
        url: '/api/quota',
        method: 'put',
        data
    })
export const delQuota = (id: any) =>
    request({
        url: `/api/quota/${id}`,
        method: 'delete'
    })

export const gps = () =>
    request({
        url: '/api/gps',
        method: 'get'
    })
export const addGps = (data: any) =>
    request({
        url: '/api/gps',
        method: 'post',
        data
    })
export const putGps = (data: any) =>
    request({
        url: '/api/gps',
        method: 'put',
        data
    })  