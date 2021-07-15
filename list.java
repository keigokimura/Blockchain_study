import java.io.*;
import javax.swing.*;

// ブラックリストに関するクラス
class List{
    public static void main(String[] args)throws IOException{
        // ファイル選択用オブジェクトJFileChhoserインスタンス生成
        JFileChooser chooser = new JFileChooser();

        // デフォルトで選択されているファイル
        chooser.setSelectedFile(new File("dataList.txt"));

        // ダイアログ表示
        chooser.showOpenDialog(null);

        File file = chooser.getSelectedFile();
        if(file!=null){
            System.out.println("選択されたファイル:"+file.getPath());
            System.out.println(getNotDeplicateresult(file));
        }
    }

    // ブラックリストの内容を取得します
    public static String getFileContent(File f)throws IOException{
        BufferedReader br = null;
        try{
            // ファイルを読み込むバッファードリーダーのインスタンス
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            // 読み込んだファイルの内容を保持する
            StringBuffer buffer = new StringBuffer();
            // ファイルから読み込んだ1文字を保持する
            int c;

            // 文字が有る限りループ
            while((c=br.read())!=-1){
                buffer.append((char) c);
            }
            return buffer.toString();
        }finally{
            br.close();
        }
    }


    // ファイルの行数を取得します
    public static int getFileIndex(File f)throws IOException{
        BufferedReader br = null;
        try{
            // ファイルを読み込むバッファードリーダーのインスタンス
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            int index = 0;
            String c = null;
            while((c=br.readLine())!=null){
                index++;
            }
            return index;
        }finally{
            br.close();
        }
    }

    // 与えられたファイルから重複をなくした結果を取得します
    public static String getNotDeplicateresult(File f)throws IOException{
        BufferedReader br = null;
        try{
            // ファイルを読み込むバッファードリーダーのインスタンス
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            // ファイルの中身を保持する文字列バッファ
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while((line=br.readLine())!=null){
                if(buffer.indexOf(line)==-1){
                    buffer.append(line);
                }
            }
            return buffer.toString();
        }finally{
            br.close();
        }
    }

}
