
module.exports = ctx => {
    const register = () => { // 插件的register
        ctx.log.error("进入注册...")
        ctx.helper.uploader.register('tmage', {
            handle,
            name:'tmage',
            config:config
        }) // 这里的transformer可以替换成uploader等你想开发的部部件的名字。
    }
    const handle = async function(ctx) {
        ctx.log.error("进入到异步提交模块")
        let userConfig = ctx.getConfig('picBed.tmage')
        const url = userConfig.url
        const key = userConfig.key
        const returnUrl = userConfig.returnUrl
        let imgList = ctx.output
        let image = imgList[0].buffer
        let fileName = imgList[0].fileName
        if (!image && imgList[0].base64Image) {
            image = Buffer.from(imgList[0].base64Image, 'base64')
        }
        await ctx.Request.request({
            method:'POST',
            url:encodeURI(url),
            formData:{
                image:image.toString('base64'),
                fileName:fileName,
                key:key
            },
            headers:{
                contentType: "application/json;charset=UTF-8;",
                'User-Agent': 'tmage'
            }
        })
        imgList[0]['imgUrl'] = returnUrl+'/'+fileName
        ctx.log.error("上传完成")
    }

    const config = ctx => {
        let userConfig = ctx.getConfig('picBed.tmage')
        if (!userConfig) {
            userConfig = {}
        }
        return [
            {
                name: 'url',
                type: 'input',
                default: userConfig.url,
                required: true,
                message: '文件提交接口地址,例如http://localhost:8081/tmage/test',
                alias: 'url'
            },
            {
                name: 'key',
                type: 'input',
                default: userConfig.key,
                required: true,
                message: 'key',
                alias: 'key'
            },
            {
                name: 'returnUrl',
                type: 'input',
                default: userConfig.returnUrl,
                required: false,
                message: '配置通过nginx访问的url,例如http://localhost:10000/images',
                alias: 'returnUrl'
            }
        ]
    }
    return {
        register,
        uploader:'tmage'
    }
}