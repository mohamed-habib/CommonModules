package codelab.android.com.commonmodules.filesutils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Nourhan on 12/26/2016.
 */

/**
 * fileFromByteArray
 * fileInputStreamToString
 * fileToInputStream
 * readFileasString
 * writeJsonToFile
 * fileToByteArray
 * zipFileToByteArray
 * copyFileOrDirFromAssetsToDeviceStorage
 * copySingleFileFromAssetToDeviceStorage
 * createFileFromInputStream
 * copyFileToDir
 * writeStringToFile
 */
public class FileModule {

    public File fileFromByteArray(byte[] byteFile, String filePath) {
        try {

            File directory = new File(Environment.getExternalStorageDirectory() + "/PME");
            if (!directory.exists())
                directory.mkdirs();

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bos.write(byteFile);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String fileInputStreamToString(FileInputStream is)  {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public FileInputStream fileToInputStream(String filePath) {
        File fl = new File(filePath);
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(fl);
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  fin;
    }

    public String readFileasString(String filePath) {
        String stringFile = null;
        FileInputStream fin = fileToInputStream(filePath);
            stringFile = fileInputStreamToString(fin);
        return stringFile;
    }

    public File writeJsonToFile(String jsonString,String generatedDirPath, String generatedFileName){
        File file = null;
        try {

            File directory = new File(generatedDirPath);
            if (!directory.exists())
                directory.mkdirs();

            file = new File(generatedDirPath+ "/"+generatedFileName);


            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF8"));

            out.write(jsonString);

            out.flush();
            out.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public byte[] fileToByteArray(String jsonString,String generatedDirPath, String generatedFileName) {

        File file = null;
        file = writeJsonToFile(jsonString, generatedDirPath,generatedFileName);

        FileInputStream crunchifyInputStream = null;
        byte[] dataArray = null;

        try {
            dataArray = new byte[(int) file.length()];
            if (file.exists()) {
                crunchifyInputStream = new FileInputStream(file);
                crunchifyInputStream.read(dataArray);
                crunchifyInputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataArray;
    }

    public byte[] zipFileToByteArray(String zippedFilePath) {
        FileInputStream inputStream = null;
        File file = null;
        byte[] dataArray = null;

        try {
            file = new File(zippedFilePath);
            dataArray = new byte[(int) file.length()];
            if (file.exists()) {
                inputStream = new FileInputStream(file);
                inputStream.read(dataArray);
                inputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataArray;
    }

    public  void copyFileOrDirFromAssetsToDeviceStorage(String assetPath, Context context, String resultPath) {
        AssetManager assetManager = context.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(assetPath);
            if (assets.length == 0) {
                //this is a file
                copySingleFileFromAssetToDeviceStorage(assetPath, resultPath, context);
            } else {
                //this is a folder
                File dir = new File(resultPath);
                if (!dir.exists())
                    dir.mkdir();
                for (String asset : assets) {
                    copyFileOrDirFromAssetsToDeviceStorage(assetPath + "/" + asset, context, resultPath);
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
        }
    }

    public  void copySingleFileFromAssetToDeviceStorage(String assetFilePath, String resultFilePath, Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(assetFilePath);
            out = new FileOutputStream(resultFilePath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public  File createFileFromInputStream(InputStream inputStream, String mFileName) {

        try {
            File f = new File(mFileName);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    public  void copyFileToDir(String filePath, String dirPath, String newFileName) {

        File taskDirectory = new File(dirPath);

        String fileName = "";

        if (newFileName != null) {
            fileName = newFileName;
        } else {
            String[] splitArr = filePath.split("/");
            fileName = splitArr[splitArr.length - 1];
        }


        String newFilePath = taskDirectory.getPath() + File.separatorChar + fileName;

        File newFile = new File(newFilePath);

        InputStream in;
        OutputStream out;
        try {
            //create output directory if it doesn't exist
            if (!taskDirectory.exists()) {
                taskDirectory.mkdirs();
            }

            if (!newFile.exists()) {
                newFile.createNewFile();
            }

            in = new FileInputStream(filePath);
            out = new FileOutputStream(newFilePath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void writeStringToFile(String data, String dirPath, String fileName) {

        File dirPathFile = new File(dirPath);
        // Make sure the path directory exists.
        if (!dirPathFile.exists()) {
            // Make it, if it doesn't exit
            dirPathFile.mkdirs();
        }

        final File file = new File(dirPathFile, fileName);
        // Save your stream, don't forget to flush() it before closing it.

        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
