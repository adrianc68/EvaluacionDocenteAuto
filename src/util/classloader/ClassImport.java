package util.classloader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassImport {

    public Object invokeClassMethod(String directoryA, String methodName) {
        Object returnObject = null;
        String directory = directoryA.replace( getFileName(directoryA), "");
        String classToLoad = getFileName(directoryA).replace(".class", "");
        File file = new File(directory);
        Class classLoaded;
        try {
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            ClassLoader classLoader = new URLClassLoader(urls);
            classLoaded = classLoader.loadClass(classToLoad);
            Constructor constructor = classLoaded.getConstructor();
            Object classObject = constructor.newInstance();
            Method method = classLoaded.getMethod(methodName);
            returnObject = method.invoke(classObject);
        } catch (MalformedURLException e) {
            e.printStackTrace(); // ???
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // ???
        } catch (InstantiationException e) {
            e.printStackTrace(); // ???
        } catch (InvocationTargetException e) {
            e.printStackTrace(); // ???
        } catch (NoSuchMethodException e) {
            e.printStackTrace(); // ???
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // ???
        }
        return returnObject;
    }

    private String getFileName(String charSequence) {
        String fileName = null;
        for( int i = charSequence.length() - 1; i > -1 ; i-- ) {
            if(charSequence.charAt(i) == '/' || charSequence.charAt(i) == '\\' || i == 0) {
                fileName = charSequence.substring( i + 1 );
                break;
            }
        }
        return fileName;
    }

    public Object loadJAR(URL jarURL, String methodName) {
        String className = getFileName( jarURL.toString() ).replace(".jar", "");
        Object result = null;
        try {
            URLClassLoader child  = new URLClassLoader( new URL[] { jarURL.toURI().toURL() }, this.getClass().getClassLoader() );
            Class classToLoad = Class.forName(className, true, child);
            Method method = classToLoad.getDeclaredMethod(methodName);
            Object instance = classToLoad.newInstance();
            result = method.invoke(instance);
        } catch (MalformedURLException | URISyntaxException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean loadLibrary() {

        return true;
    }

}
