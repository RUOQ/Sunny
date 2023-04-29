package com.ruoq.network.interceptor

import com.ruoq.network.annotation.BaseUrl
import com.ruoq.network.annotation.Ignore
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.Retrofit
import java.lang.reflect.Method

class BaseUrlInterceptor(
    private val retrofitProvider:() -> Retrofit
):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        with(chain.request()){
            val baseUrlAnnotation = getBaseUrlAnnotationUrl() ?:
            return chain.proceed(this)
            //能走到这里则代表我们已经获取到BaseUrl注解，开始替换BaseUrl的操作
            val newUrl = getNewUrlOrNothing(baseUrlAnnotation)
            val newRequest = newBuilder()
                .url(newUrl)
                .build()
            return chain.proceed(newRequest)
        }


    }


    /**
     *  获取新的完整请求的url
     */
    private fun Request.getNewUrlOrNothing(baseUrlAnnotation:BaseUrl):String{

        /**
         * 先获取retrofit示例
         */
        val retrofit = retrofitProvider.invoke()
        //我们先拿到当前请求url 地址， (该地址是全路径地址)
        val oldUrl = url.toString()
        val httpUrl = retrofit.baseUrl()
        //拿到retrofit实例的baseurl
        val baseUrl = httpUrl.toString()
        //如果oldUrl 中包含了retrofit的base url 则代表需要被替换
        return takeUnless { oldUrl.indexOf(baseUrl) == -1 }?.let {
            //执行替换操作，拿到不包含baseUrl的部分路径
            val suffixUrl = oldUrl.replaceFirst(baseUrl,"")
            baseUrlAnnotation.value + suffixUrl
        }?:oldUrl

    }

    /**
     * 通过Request 对象获取BaseUrl注解，如果获取不到则返回 null
     */
    private fun Request.getBaseUrlAnnotationUrl():BaseUrl?{
        val invocation = tag(Invocation::class.java)?:return null
        //获取到当前请求的Method对象
        val method = invocation.method()
        //如果当前请求包含了ignore 注解，则不做处理
        method.getIgnoreAnnotationOrNull()?.let{
            return null
        }
        return method.getBaseUrlAnnotationOrNull()?:method.declaringClass.getBaseUrlAnnotationOrNull()
    }

    /**
     * 获取Ignore注解,如果获取不到则返回 null
     */
    private fun Method.getIgnoreAnnotationOrNull():
            Ignore?=getAnnotation(Ignore::class.java)


    /**
     * 获取BaseUrl 注解， 如果获取不到则返回 null
     */
    private fun Class<*>.getBaseUrlAnnotationOrNull():
            BaseUrl? = annotations.firstOrNull()



    /**
     * 获取到 BaseUrl 注解，如果获取不到则返回 `null`
     */
    private fun Method.getBaseUrlAnnotationOrNull(): BaseUrl? = annotations.firstOrNull()

    /**
     * 通过遍历获取到第一个指定类型的注解，如果获取不到则返回null
     */
    private inline fun <reified T:Annotation> Array<Annotation>.firstOrNull():T ?{
        forEach { annotation ->
            annotation.getCustomAnnotationOrNull<T>().takeUnless{
                it == null
            }?.let{
                return it
            }
        }
        return null
    }

    /**
     * 获取到自定义注解，如果获取不到则返回null
     */
    private inline fun <reified T: Annotation>
            Annotation.getCustomAnnotationOrNull(): T?=
        annotationClass.java.getAnnotation(T::class.java)
}