import request from '@/utils/request'

export const getDevice = (params: any) =>
    request({
        url: '/api/device',
        method: 'get',
        params
    })
export const deviceStatus = (data: any) =>
    request({
        url: '/api/device/status',
        method: 'put',
        data
    })

export const device = (params: any) =>
request({
    url: '/api/device/deviceQuota',
    method: 'get',
    params
})    
