package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import mitlab.seg.MengNER;
import mitlab.seg.ner.domain.Term;

public class MengSeg_Main_Controller {
  @FXML
  private Button runAll;
  @FXML
  private TextArea result;
  @FXML
  private TextArea result2;
  @FXML
  private Button runAllFile;
  @FXML
  private TextArea text;
  @FXML
  private RadioButton addFinance;
  @FXML
  private RadioButton addLaw;
  @FXML
  private RadioButton addMedicine;
  @FXML
  private CheckBox CWS;
  @FXML
  private CheckBox POSTagging;
  @FXML
  private CheckBox NER;
  @FXML
  private RadioButton addFinanceFile;
  @FXML
  private RadioButton addLawFile;
  @FXML
  private RadioButton addMedicineFile;
  @FXML
  private CheckBox CWSFile;
  @FXML
  private CheckBox POSTaggingFile;
  @FXML
  private CheckBox NERFile;
  @FXML
  private TextField inputFile;
  @FXML
  private TextField outputPath;
  @FXML
  private Button chooseInput;
  @FXML
  private Button chooseOutput;
  @FXML
  private CheckBox addRuler;
  @FXML
  private TextField model_text;
  @FXML
  private Button learn;
  @FXML
  private Button clear_ruler;
  @FXML
  private RadioButton origin;

  @FXML
  private Button refresh_file;

  @FXML
  private RadioButton origin_file;

  public static List<String> model = new ArrayList<String>();
  public static String filepath = "";
  @FXML
  protected void RunAllSingleLine(ActionEvent event) throws IOException {
    if(text.getText().equals("")) {
      return;
    }
    int choose = 0;
    result.setText("");
    if (addFinance.isSelected()) {
      choose = 1;
    } else if (addLaw.isSelected()) {
      choose = 2;
    } else if (addMedicine.isSelected()) {
      choose = 3;
    } else if (origin.isSelected()) {
      choose = 0;
    }
    String tmp = "";

    if (addRuler.isSelected() && !model.isEmpty()) {
      for(String s : model) {
        if(!s.isEmpty()) {
          MengSeg_Main.cws.addRule(s);
        }
      }
      if (!POSTagging.isSelected() && !NER.isSelected()) {
        List<String> termList = MengSeg_Main.cws.SegRet(choose, text.getText());
        for (int i = 0; i < termList.size() - 1; i++) {
          tmp += (termList.get(i) + " ");
        }
        tmp += termList.get(termList.size() - 1);
      } else if (POSTagging.isSelected()) {
        List<String> termList = MengSeg_Main.cws.SegRet(choose, text.getText());
        List<String> tagged = MengSeg_Main.postagging.POSTagging(termList);
        for (int i = 0; i < termList.size() - 1; i++) {
          tmp += (termList.get(i) + "/" + tagged.get(i) + " ");
        }
        tmp += termList.get(termList.size() - 1) + "/" + tagged.get(tagged.size() - 1);
      }
    } else {
      if (!POSTagging.isSelected() && !NER.isSelected()) {
        List<String> termList = MengSeg_Main.cws.SegRet(choose, text.getText());
        for (int i = 0; i < termList.size() - 1; i++) {
          tmp += (termList.get(i) + " ");
        }
        tmp += termList.get(termList.size() - 1);
      } else if (POSTagging.isSelected()) {
        List<String> termList = MengSeg_Main.cws.SegRet(choose, text.getText());
        List<String> tagged = MengSeg_Main.postagging.POSTagging(termList);
        for (int i = 0; i < termList.size() - 1; i++) {
          tmp += (termList.get(i) + "/" + tagged.get(i) + " ");
        }
        tmp += termList.get(termList.size() - 1) + "/" + tagged.get(tagged.size() - 1);
      }
    }

    if (NER.isSelected()) {
      List<Term> ner = MengNER.NER(text.getText(), choose);
      for (int i = 0; i < ner.size(); i++) {
        String law_l = ner.get(i).getNatureStr();
        if (law_l.equals("l")) {
          law_l = "law";
        }
        tmp += ner.get(i).getRealName() + "/" + law_l + "\t起点：" + ner.get(i).getOffe() + "\n";
      }
    }
    result.setText(tmp);
  }

  @FXML
  protected void RunAllFile(ActionEvent event) throws IOException {
    result2.setText("");
    if (inputFile.getText().equals("")) {
      f_alert_confirmDialog("请先选择输入文件！", null);
      return;
    }
    String[] inputfile = inputFile.getText().substring(0,inputFile.getText().length()-1).split(";");
    String outputpath = outputPath.getText().toString();
    if(outputpath.equals("") || outputpath == null) {
      outputpath = filepath;
    }
    int choose = 0;
    if (addFinanceFile.isSelected()) {
      choose = 1;
    } else if (addLawFile.isSelected()) {
      choose = 2;
    } else if (addMedicineFile.isSelected()) {
      choose = 3;
    } else if (origin_file.isSelected()) {
      choose = 0;
    }
    String tmp = "";
    if (!POSTaggingFile.isSelected() && !NERFile.isSelected()) {
      result2.setText("");
      String str = "开始对以下文件分词：";
      for(String s:inputfile) {
        str+="\n\t"+s;
      }
      str += "\n";
      result2.setText(str);
      for(String file : inputfile) {
        File f = new File(file);
        File f2 = new File(outputpath+f.getName()+".cws");
        Writer w = new FileWriter(f2,true);
        BufferedReader bre = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
        String strs = null;
        while ((strs = bre.readLine()) != null) {
          if(strs.equals("")) {
            w.write("\n");
            continue;
          }
          String tmp_line = "";
          List<String> termList = MengSeg_Main.cws.SegRet(choose, strs);
          for (int i = 0; i < termList.size() - 1; i++) {
            tmp_line += (termList.get(i) + " ");
          }
          tmp_line += termList.get(termList.size() - 1)+"\n";
          w.write(tmp_line);
        }
        bre.close();
        w.close();
      }
      str = "分词结果已经写入以下目录：";
      str += "\n\t"+outputpath;
      str += "\n";
      result2.setText(result2.getText()+str);
    } else if (POSTaggingFile.isSelected()) {
      result2.setText("");
      String str = "开始对以下文件分词并词性标注：";
      for(String s:inputfile) {
        str+="\n\t"+s;
      }
      str += "\n";
      result2.setText(str);
      for(String file : inputfile) {
        File f = new File(file);
        File f2 = new File(outputpath+f.getName()+".cws+pos");
        Writer w = new FileWriter(f2,true);
        BufferedReader bre = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
        String strs = null;
        while ((strs = bre.readLine()) != null) {
          if(strs.equals("")) {
            w.write("\n");
            continue;
          }
          String tmp_line = "";
          List<String> termList = MengSeg_Main.cws.SegRet(choose, strs);
          List<String> tagged = MengSeg_Main.postagging.POSTagging(termList);
          for (int i = 0; i < termList.size() - 1; i++) {
            tmp_line += (termList.get(i) + "/" + tagged.get(i) + " ");
          }
          tmp_line += termList.get(termList.size() - 1) + "/" + tagged.get(tagged.size() - 1)+"\n";
          w.write(tmp_line);
        }
        bre.close();
        w.close();
      }
      str = "分词及词性标注结果已经写入以下目录：";
      str += "\n\t"+outputpath;
      str += "\n";
      result2.setText(result2.getText()+str);
    }
    if (NERFile.isSelected()){
      String str = "开始对以下文件进行命名实体识别：";
      for(String s:inputfile) {
        str+="\n\t"+s;
      }
      str += "\n";
      result2.setText(result2.getText()+str);
      for(String file : inputfile) {
        File f = new File(file);
        File f2 = new File(outputpath+f.getName()+".ner");
        Writer w = new FileWriter(f2,true);
        BufferedReader bre = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
        String strs = null;
        while ((strs = bre.readLine()) != null) {
          if(strs.equals("")) {
            w.write("\n");
            continue;
          }
          String tmp_line = "";
          List<Term> ner = MengNER.NER(strs, choose);
          for (int i = 0; i < ner.size(); i++) {
            String law_l = ner.get(i).getNatureStr();
            if (law_l.equals("l")) {
              law_l = "law";
            }
            tmp_line += ner.get(i).getRealName() + "/" + law_l + " From：" + ner.get(i).getOffe() + "\t";
          }
          w.write(tmp_line+"\n");
        }
        bre.close();
        w.close();
      }
      str = "命名实体识别结果已经写入以下目录：";
      str += "\n\t"+outputpath;
      str += "\n";
      result2.setText(result2.getText()+str);
    }
    result2.setText(result2.getText()+"\n运行完成！\n");
  }

  @FXML
  protected void ChooseInputFile(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    List<File> list = fileChooser.showOpenMultipleDialog(null);
    if (list != null) {
      String input = "";
      for (File f : list) {
        input += f.getAbsolutePath().toString() + ";";
      }
      File tmp = list.get(0);
      String filename = tmp.getName();
      filepath = tmp.getAbsolutePath().substring(0,tmp.getAbsolutePath().indexOf(filename));
      inputFile.setText(input);
    }
  }

  @FXML
  protected void ChooseOutputPath(ActionEvent event) {
    DirectoryChooser chooser = new DirectoryChooser();
    File chosenDir = chooser.showDialog(null);
    if (chosenDir != null) {
      outputPath.setText(chosenDir.getAbsolutePath());
    }
  }

  @FXML
  protected void AddLearnRuler(ActionEvent event) {
    if (!model_text.getText().isEmpty()) {
      model.add(model_text.getText());
      model_text.setText("");
    }
  }

  @FXML
  protected void ClearRuler(ActionEvent event) {
    if(model.isEmpty()) {
      return;
    }
    String tmp = "";
    for (String s : model) {
      tmp += s + "\n";
    }
    if (f_alert_confirmDialog("以下学习规则将被删除:", tmp)) {
      model = new ArrayList<String>();
    }
  }

  @FXML
  protected void Refresh(ActionEvent event) {
    if (f_alert_confirmDialog("所有内容将会清空！", null)) {
      model = new ArrayList<String>();
      origin.setSelected(true);
      text.setText("");
      result.setText("");
      CWS.setSelected(true);
      NER.setSelected(false);
      POSTagging.setSelected(false);
      addRuler.setSelected(false);
      model_text.setText("");
    }
  }

  @FXML
  protected void RefreshFile(ActionEvent event) {
    if (f_alert_confirmDialog("所有内容将会清空！", null)) {
      origin_file.setSelected(true);
      inputFile.setText("");
      outputPath.setText("");
      CWSFile.setSelected(true);
      NERFile.setSelected(false);
      POSTaggingFile.setSelected(false);
    }
  }

  public boolean f_alert_confirmDialog(String title, String p_message) {
    Alert _alert = new Alert(Alert.AlertType.CONFIRMATION);
    _alert.setHeaderText(title);
    _alert.setContentText(p_message);
    Optional<ButtonType> _buttonType = _alert.showAndWait();
    if (_buttonType.get().getButtonData().toString().equals("OK_DONE")) {
      return true;
    } else {
      return false;
    }
  }
}
