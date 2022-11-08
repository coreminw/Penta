import java.io.*;

public class Main {

    public static String hexFilePath;
    public static String originFilePath;
    public static String destFilePath;

    public static void main(String[] args) {
        try {
            originFilePath = "D:\\1-1. PClient 사용방법.pdf";
            hexFilePath = "D:\\memoPdf.txt";
            destFilePath = "D:\\1-1. PClient 사용방법2.pdf";

            File file = new File(originFilePath);
            File file2 = new File(destFilePath);
            if(file2.exists()){
                System.out.println("file exist!");
                return;
            }

            // 0. pdf를 hex로 읽고 출력하기
//            ConvertPdfToHex(originFilePath);

            // 1. 바이너리 파일을 hex 형태로 읽기
            String hex = readBinaryFileToHex(file);

            // 2. hex 문자열을 텍스트 파일로 쓰기 (확인용)
//            writeTextFile(hexFilePath, hex);

            // 3. hex 문자열을 binaryFile로 만들기
            writeBinaryFileFromHex(destFilePath, hex);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 0. pdf를 hex로 읽고 출력하기
    public static void ConvertPdfToHex(String originFilePath) throws IOException {
        FileInputStream fileis = new FileInputStream(originFilePath);
        BufferedInputStream bis = new BufferedInputStream(fileis);
        byte[] arrByte = new byte[bis.available()];

        int ioffs = 0; // 번지(라인)
        int iLine; // 길이
        String space = "   "; // 자리수 맞출 공백

        System.out.println("iLine:" + ((iLine = bis.read(arrByte))));

        if (iLine > 0) {

            int end = (iLine / 16) + (iLine % 16 != 0 ? 1 : 0); //전체 줄수를 구함.
            System.out.println("end:" + end);

            for (int i = 0; i < end; i++) {

                //번지 출력
                System.out.format("%08X: ", ioffs); // Offset : 번지 출력

                //헥사구역
                for (int j = ioffs; j < ioffs + 16; j++) { //16개 출력

                    if (j < iLine) {
                        System.out.format("%02X ", arrByte[j]); // 헥사 값 2개출력 , %x 16진수
                    } else {
                        //System.out.printf("%3S"," "); // 공백처리기능
                        System.out.print(space);
                    }

                }

                //문자구역
                for (int k = ioffs; k < ioffs + 16; k++) { // 문자길이

                    if (k == iLine)    break;

                    if (arrByte[k] < 32 || arrByte[k] > 127) { // 특수문자를 . 으로변환
                        System.out.print(".");
                    } else {
                        System.out.format("%c", arrByte[k]); // 문자  ( 32 ~ 127)
                    }
                }

                ioffs += 16; //번지수 증가.
                System.out.println(); //줄 바꿈.

            }
        }

        bis.close();
        fileis.close();

    }

    /**
     * 바이너리 파일을 hex 형태로 읽기 (file to hex 문자열)
     *
     * @param file
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String readBinaryFileToHex(File file) throws IOException, Exception {
        if (file == null || !file.exists()) {
            return null;
        }

        StringBuffer hexBuff = new StringBuffer();

        FileInputStream fileInputStream = null;
        DataInputStream inputStreamReader = null;

        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new DataInputStream(fileInputStream);

            byte[] arrByte = new byte[1024];
            int len = 0; // 실제로 읽어온 길이 (바이트 개수)

            while ((len = inputStreamReader.read(arrByte)) > 0) {
                for (int i = 0; i < len; i++) {
                    hexBuff.append(String.format("%02X", arrByte[i]).trim());
                }
            }

        } catch (IOException e) {
            throw e;

        } catch (Exception e) {
            throw e;

        } finally {
            close(inputStreamReader);
            close(fileInputStream);
        }

        System.out.println(hexBuff);
        return hexBuff.toString();
    }

    /**
     * hex 문자열을 binaryFile로 만들기
     *
     * @param filePath
     * @param hex
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static boolean writeBinaryFileFromHex(String filePath, String hex) throws IOException, Exception {
        if (filePath == null || filePath.length() == 0) {
            return false;
        }

        if (hex == null) {
            hex = "";
        }

        boolean isWrite = false;

        File file = new File(filePath);

        FileOutputStream fileOutputStream = null;
        DataOutputStream outputStreamWriter = null;

        try {
            boolean isAppend = false;
            fileOutputStream = new FileOutputStream(file, isAppend);
            outputStreamWriter = new DataOutputStream(fileOutputStream);

            int arrayLen = hex.length() / 2;
            int[] b = new int[arrayLen];

            // hex string to int array
            int c = 0;
            int len = hex.length();
            for (int i = 0; i < len; i = i + 2) {
                b[c] = Integer.parseInt(hex.substring(i, i + 2), 16);
                c++;
            }

            // int array to byte
            for (int i = 0; i < b.length; i++) {
                outputStreamWriter.write(b[i]);
            }

            isWrite = true;

        } catch (IOException e) {
            throw e;

        } catch (Exception e) {
            throw e;

        } finally {
            close(outputStreamWriter);
            close(fileOutputStream);
        }

        return isWrite;
    }

    /**
     * 문자열을 텍스트 파일로 쓰기
     *
     * @param filePath
     * @param content
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static boolean writeTextFile(String filePath, String content) throws IOException, Exception {
        boolean isWrite = false;

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            isWrite = true;

        } catch (IOException e) {
            throw e;

        } catch (Exception e) {
            throw e;

        } finally {
            close(fileWriter);
        }
        System.out.println("file created!");
        return isWrite;
    }

    private static void close(FileWriter obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (Exception e) {
        } finally {
            obj = null;
        }
    }

    private static void close(DataOutputStream obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (Exception e) {
        } finally {
            obj = null;
        }
    }

    private static void close(DataInputStream obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (Exception e) {
        } finally {
            obj = null;
        }
    }

    private static void close(FileInputStream obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (Exception e) {
        } finally {
            obj = null;
        }
    }

    private static void close(FileOutputStream obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (Exception e) {
            obj = null;
        }
    }
}