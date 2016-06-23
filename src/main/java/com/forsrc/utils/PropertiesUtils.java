package com.forsrc.utils;


import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public final class PropertiesUtils {


    private PropertiesUtils() {

    }

    /**
     * @param file
     * @param key
     * @param charset
     * @return String
     * @throws IOException
     * @throws
     * @Title: getPropertiesVaule
     * @Description:
     */
    public static String getPropertiesVaule(File file, String key,
                                            Charset charset) throws IOException {
        if (file == null) {
            return null;
        }
        if (charset == null) {
            charset = Charset.forName("ISO8859-1");
        }
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            properties.load(fis);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new IOException(e);
                } finally {
                    if (properties != null) {
                        properties.clear();
                    }
                }
            }
        }
        String value = properties.getProperty(new String(key.getBytes(charset),
                "ISO8859-1"));
        if (properties != null) {
            properties.clear();
        }
        return value == null ? null : new String(value.getBytes("ISO8859-1"),
                charset);
    }

    /**
     * @param file
     * @param key
     * @return String
     * @throws IOException
     * @throws
     * @Title: getPropertiesVaule
     * @Description:
     */
    public static String getPropertiesVaule(File file, String key)
            throws IOException {
        return getPropertiesVaule(file, key, null);
    }

    /**
     * @param @param  file
     * @param @param  key
     * @param @param  value
     * @param @return
     * @return String
     * @throws IOException
     * @throws
     * @Title: setPropertiesVaule
     * @Description:
     */
    public static String setPropertiesVaule(File file, String key, String value)
            throws IOException {
        return setPropertiesVaule(file, key, value, file.getPath());
    }


    /**
     * @param file
     * @param key
     * @param value
     * @param comments
     * @return String
     * @throws IOException
     * @throws
     * @Title: setPropertiesVaule
     * @Description:
     */
    public static String setPropertiesVaule(File file, String key,
                                            String value, String comments) throws IOException {
        return setPropertiesVaule(file, key, value, comments, null);
    }

    /**
     * @param file
     * @param key
     * @param value
     * @param comments
     * @return String
     * @throws IOException
     * @throws
     * @Title: setPropertiesVaule
     * @Description:
     */
    public static String setPropertiesVaule(File file, String key,
                                            String value, String comments, Charset charset) throws IOException {
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        if (charset == null) {
            charset = Charset.forName("ISO8859-1");
        }
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            properties.load(fis);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        Object v = properties.setProperty(new String(key.getBytes(charset),
                "ISO8859-1"), new String(value.getBytes(charset), "ISO8859-1"));

        BufferedWriter bw = null;
        try {
            Set set = properties.entrySet();
            Iterator<Object> iterator = set.iterator();
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("#" + comments);
            bw.newLine();
            bw.write("#" + new Date().toString());
            bw.newLine();
            while (iterator.hasNext()) {
                String str = iterator.next().toString();
                bw.write(new String(str.getBytes("ISO8859-1"), charset));
                bw.newLine();
            }
            bw.flush();
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        } finally {
            if (bw != null) {
                bw.close();
            }
            if (properties != null) {
                properties.clear();
            }
        }

        return v == null ? null : new String(
                v.toString().getBytes("ISO8859-1"), charset);
    }

    public static Properties getProperties(String propertiesFileName)
            throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtils.class.getResource(
                    propertiesFileName).openStream());
            return properties;
        } catch (IOException e) {
            throw e;
        }
    }

}
