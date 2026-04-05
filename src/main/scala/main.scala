import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import java.io.{File, PrintWriter}

object Main extends App {

  val spark = SparkSession.builder()
    .appName("LogsPipeline")
    .master("local[1]")
    .config("spark.sql.shuffle.partitions", "1")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  val df = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("data/logs.csv")

  val dfClean = df
    .dropDuplicates()
    .na.drop()
    .filter(col("status").isNotNull)
    .filter(col("response_time_ms") > 0)
    .filter(col("bytes") > 0)

  val dfEnriched = dfClean
    .withColumn("hour", hour(col("timestamp")))
    .withColumn("date", to_date(col("timestamp")))
    .withColumn("is_error", col("status") >= 400)
    .withColumn("response_category",
      when(col("response_time_ms") < 200, "rapide")
      .when(col("response_time_ms") < 800, "normal")
      .otherwise("lent"))

  println("\n=== TRANSFORMATIONS ===")

  println("\n--- Top 5 pages les plus consultees ---")
  dfEnriched.groupBy("url").count().orderBy(desc("count")).show(5)

  println("\n--- Repartition des codes HTTP ---")
  dfEnriched.groupBy("status").count().orderBy(desc("count")).show()

  println("\n--- Trafic par pays ---")
  dfEnriched.groupBy("country")
    .agg(count("*").alias("nb_requetes"), round(avg("response_time_ms"), 2).alias("temps_moyen_ms"))
    .orderBy(desc("nb_requetes")).show()

  println("\n--- Taux d'erreur par URL ---")
  dfEnriched.groupBy("url")
    .agg(count("*").alias("total"), sum(col("is_error").cast("int")).alias("erreurs"))
    .withColumn("taux_erreur_%", round(col("erreurs") * 100 / col("total"), 2))
    .orderBy(desc("taux_erreur_%")).show()

  println("\n--- Trafic par heure ---")
  dfEnriched.groupBy("hour").count().orderBy("hour").show(24)

  println("\n--- Top 5 IPs les plus actives ---")
  dfEnriched.groupBy("ip")
    .agg(count("*").alias("nb_requetes"), sum(col("is_error").cast("int")).alias("nb_erreurs"))
    .orderBy(desc("nb_requetes")).show(5)

  println("\n=== STOCKAGE DES RESULTATS ===")

  def saveCSV(data: Array[String], path: String): Unit = {
    new File(path).getParentFile.mkdirs()
    val pw = new PrintWriter(new File(path))
    data.foreach(pw.println)
    pw.close()
  }

  val topPages = dfEnriched.groupBy("url").count().orderBy(desc("count")).collect()
  saveCSV(
    Array("url,count") ++ topPages.map(r => s"${r.getString(0)},${r.getLong(1)}"),
    "output/top_pages.csv"
  )

  val parPays = dfEnriched.groupBy("country")
    .agg(count("*").alias("nb_requetes"), round(avg("response_time_ms"), 2).alias("temps_moyen_ms"))
    .orderBy(desc("nb_requetes")).collect()
  saveCSV(
    Array("country,nb_requetes,temps_moyen_ms") ++ parPays.map(r => s"${r.getString(0)},${r.getLong(1)},${r.getDouble(2)}"),
    "output/trafic_par_pays.csv"
  )

  val codesHttp = dfEnriched.groupBy("status").count().orderBy(desc("count")).collect()
  saveCSV(
    Array("status,count") ++ codesHttp.map(r => s"${r.getInt(0)},${r.getLong(1)}"),
    "output/codes_http.csv"
  )

  val parHeure = dfEnriched.groupBy("hour").count().orderBy("hour").collect()
  saveCSV(
    Array("hour,count") ++ parHeure.map(r => s"${r.getInt(0)},${r.getLong(1)}"),
    "output/trafic_par_heure.csv"
  )

  val topIPs = dfEnriched.groupBy("ip")
    .agg(count("*").alias("nb_requetes"), sum(col("is_error").cast("int")).alias("nb_erreurs"))
    .orderBy(desc("nb_requetes")).collect()
  saveCSV(
    Array("ip,nb_requetes,nb_erreurs") ++ topIPs.map(r => s"${r.getString(0)},${r.getLong(1)},${r.getLong(2)}"),
    "output/top_ips.csv"
  )

  println("Donnees sauvegardees dans output/")

  spark.stop()
  println("\nPipeline terminee avec succes !")

}