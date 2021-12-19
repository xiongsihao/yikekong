import request from '@/utils/request'

//获取系统面板
export const boardSystemBoard = () =>
    request({
        url: '/api/board/systemBoard',
        method: 'get',
    })
//更改系统面板
export const boardStatus = (params:any) =>
request({
    url: '/api/board/status',
    method: 'put',
    data:params
})
    
// 添加面板    
export const addBoard = (params:any) =>
request({
    url: `/api/board`,
    method: 'post',
    data:params
}) 
// 删除面板    
export const delBoard = (id:number) =>
request({
    url: `/api/board/${id}`,
    method: 'delete',
})    
//获取非系统面板
export const getUnSystemPanel = () =>
    request({
        url: '/api/board',
        method: 'get',
    })
// 监控设备数 - 设备数和报警设备数量
export const monitor = () =>
    request({
        url: '/api/report/monitor',
        method: 'get',
    })
// 设备状态分布 - 首页环形图数据    
export const statusCollect = (params: any) =>
    request({
        url: '/api/report/statusCollect',
        method: 'get',
        params
    })
// 地图 - 根据经纬度获取设备信息
export const deviceList = (params: any) =>
    request({
        url: `/api/gps/deviceList/${params.lat}/${params.lon}/${params.distance}`,
        method: 'get',
        params
    })
// 地图 - 查询设备详情
export const deviceInfo = (params: any) =>
    request({
        url: '/api/device/deviceInfo/:deviceId',
        method: 'get',
        params
    })
// 今日异常设备
// 异常趋势图
export const trendDatas = (params: any) =>
    request({
        url: `/api/report/trend/${params.startTime}/${params.endTime}/${params.type}`,
        method: 'get',
        params
    })
// 异常数量Top10
export const top10Alarm = (params: any) =>
    request({
        url: `/api/report/top10Alarm/${params.startTime}/${params.endTime}`,
        method: 'get',
        params
    })
// 自定义面板 - 列表 
export const boardList = (params: any) =>
    request({
        url: '/board',
        method: 'get'
    })
// 自定义面板 - 用列表中的对应ID获取信息
export const boardData = (params: any) =>
    request({
        url: `/api/report/boardData/${params.id}/${params.startTime}/${params.endTime}/${params.type}`,
        method: 'get',
        params
    })
//获取指标
export const quotaNumberQuota = () =>
    request({
        url: `/api/quota`,
        method: 'get',
    })
//通过指标查询设备列表
export const quotaDevices = (params: any) =>
    request({
        url: `/api/report/devices`,
        method: 'get',
        params
    })
//添加看板
export const board = (data: any) =>
    request({
        url: '/api/board',
        method: 'post',
        data
    })
// 预览接口
export const preview = (data: any) =>
    request({
        url: '/api/report/preview',
        method: 'post',
        data
    })
