package com.example.pomeloassignment.source



import com.example.pomeloassignment.data.ResponseDataModel
import com.google.gson.Gson
import retrofit2.Response
import java.io.File
import java.io.InputStream


class LocalDataSourceTest:DataSource{

    private fun loadJSONData(filename: String): String {
        return try {
            val inputStream: InputStream = this.javaClass.getClassLoader().getResourceAsStream(filename)
           return String(inputStream.readBytes(), charset("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    inline fun getResponse(filename: String): String {
        val br = File("src/test/assets/$filename").inputStream().bufferedReader()
        val sb = StringBuilder()
        var line: String? = br.readLine()
        while (line != null) {
            sb.append(line)
            line = br.readLine()
        }
        br.close()
        println(sb.toString())
        return sb.toString()
        //return gson.fromJson(sb.toString(), T::class.java)
    }

    override suspend fun getPickupLocations(): Response<ResponseDataModel> {
        val gson=Gson()
        val dataModel=gson.fromJson<ResponseDataModel>(getResponse("pickups.json"),ResponseDataModel::class.java)
        return Response.success(dataModel)
    }
}
