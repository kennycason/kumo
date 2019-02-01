package com.kennycason.kumo.abst;

public final class Platform {

    private Platform(){}

    public static final String PACKAGE;

    static {
        boolean isAndroid;
        try{
            //Just try to access an Android class to see what platform we're on.
            //The class is either not found (runtime on a system where we need to use AWT)
            //or a stub exception is thrown (during test, when Maven still provides the stub framework)
            Class.forName("android.graphics.Canvas").newInstance();

            isAndroid = true;
        }catch (Exception ex){
            isAndroid = false;
        }

        String basePckg = "com.kennycason.kumo";

        if(isAndroid){
            PACKAGE = basePckg + ".androidimpl.";
        }else{
            PACKAGE = basePckg + ".awtimpl.";
        }
    }
}
