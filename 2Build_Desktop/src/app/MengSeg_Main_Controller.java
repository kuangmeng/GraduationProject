package app;

import java.io.IOException;
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
import uno.meng.MengNER;
import uno.meng.MengPOSTagging;
import uno.meng.MengSeg;
import uno.meng.crf_seg.library.CrfLibrary;
import uno.meng.ner.domain.Term;

public class MengSeg_Main_Controller {
  public static MengSeg cws = CrfLibrary.get();

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

  public static List<String> model = new ArrayList<String>();

  @FXML
  protected void RunAllSingleLine(ActionEvent event) throws IOException {
    int choose = 0;
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
      if (!POSTagging.isSelected() && !NER.isSelected()) {
        List<String> termList = cws.AnotherSegRet(choose, text.getText(), model);
//        for (String m : model) {
//          System.out.println(m);
//        }
        for (int i = 0; i < termList.size() - 1; i++) {
          tmp += (termList.get(i) + " ");
        }
        tmp += termList.get(termList.size() - 1);
      } else if (POSTagging.isSelected()) {
        List<String> termList = cws.AnotherSegRet(choose, text.getText(), model);
        List<String> tagged = MengPOSTagging.POSTagging(termList);
        for (int i = 0; i < termList.size() - 1; i++) {
          tmp += (termList.get(i) + "/" + tagged.get(i) + " ");
        }
        tmp += termList.get(termList.size() - 1) + "/" + tagged.get(tagged.size() - 1);
      }
    } else {
      if (!POSTagging.isSelected() && !NER.isSelected()) {
        List<String> termList = cws.SegRet(choose, text.getText());
        for (int i = 0; i < termList.size() - 1; i++) {
          tmp += (termList.get(i) + " ");
        }
        tmp += termList.get(termList.size() - 1);
      } else if (POSTagging.isSelected()) {
        List<String> termList = cws.SegRet(choose, text.getText());
        List<String> tagged = MengPOSTagging.POSTagging(termList);
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
  protected void RunAllFile(ActionEvent event) {
    result2.setText("Hello");
  }

  @FXML
  protected void ChooseInputFile(ActionEvent event) {
    result2.setText("Hello World, I am JavaFX!");
  }

  @FXML
  protected void ChooseOutputPath(ActionEvent event) {
    result2.setText("Hello World, I am JavaFX!");
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
    String tmp = "";
    for (String s : model) {
      tmp += s + "\n";
    }
    if (f_alert_confirmDialog("以下学习规则将被删除:",tmp)) {
      model = new ArrayList<String>();
    }
  }

  @FXML
  protected void Refresh(ActionEvent event) {
    if(f_alert_confirmDialog("所有内容将会清空！",null)) {
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

  /**
   * 弹出一个通用的确定对话框
   * 
   * @param p_header 对话框的信息标题
   * @param p_message 对话框的信息
   * @return 用户点击了是或否
   */
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
