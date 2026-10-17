package com.kennycason.kumo.nlp.normalize

class UpsideDownNormalizer : Normalizer {

    override fun apply(input: String): String = buildString {
        for (letter in input) {
            val index = NORMAL.indexOf(letter)
            append(if (index != -1) SPLIT[index] else letter)
        }
    }.reversed()

    companion object {
        @JvmStatic
        private val NORMAL = "abcdefghijklmnopqrstuvwxyz_,;.?!/\\'" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "0123456789"
        @JvmStatic
        private val SPLIT = "\u0250q\u0254p\u01DD\u025Fb\u0265\u0131\u0638\u029F\u05DF\u026Fu\u006F\u0064b\u0279s\u0287n\u028C\u028Dx\u028Ez\u203E'\u061B\u02D9\u00BF\u00A1/\\," +
                "\u2200q\u03BDD\u1E04\u018E\u2132\u0183HI\u017F\u029F\u02E5WNO\u0500\u1F49\u1D1AS\u22A5\u2229\u039BMXY\u028EZ" +
                "0\u0196\u1205\u0190\u3123\u03DB9\u3125\u0038\u0036"
    }
}
