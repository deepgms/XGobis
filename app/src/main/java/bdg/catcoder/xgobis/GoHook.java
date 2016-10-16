package bdg.catcoder.xgobis;

import java.util.List;
import java.util.Iterator;
import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import de.robv.android.xposed.IXposedHookLoadPackage;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;


import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;



public class GoHook implements IXposedHookLoadPackage {

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.nostratech.gojek.driver"))
            return;
        XposedBridge.log("Gobis Is Running, Ready To Work !");

        findAndHookMethod("android.provider.Settings.Secure", lpparam.classLoader, "getString", ContentResolver.class,
                String.class, new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals("mock_location"))
                            param.setResult("0");
                        XposedBridge.log("Mock Location Hooked!");
                    }


                }
        );

        findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getInstalledApplications", int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        List<ApplicationInfo> packages = (List<ApplicationInfo>) param.getResult();
                        Iterator<ApplicationInfo> iter = packages.iterator();
                        ApplicationInfo tempAppInfo;
                        String tempPackageName;
                        Object Aduh = param.getResult();
                        XposedBridge.log("Im On App Forbident List!");
                        while(iter.hasNext()) {
                            tempAppInfo = iter.next();
                            tempPackageName = tempAppInfo.packageName;
                            if(tempPackageName != null ) {
                                iter.remove();
                                XposedBridge.log("Clear Them All Baby!");

                            }
                        }
                        param.setResult(packages);

                    }


                }
        );
    }
}