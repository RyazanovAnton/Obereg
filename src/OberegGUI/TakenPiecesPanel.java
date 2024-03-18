package OberegGUI;

import OberegEngine.Board.Board;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
// Область экрана, на которой отображаются данные по количеству
// воинов обеих команд и результат игры
public class TakenPiecesPanel extends JLabel {
    private JLabel jlSlavsTeam;
    private JLabel jlVikingTeam;
    private JLabel jlSlavsTeamCount;
    private JLabel jlVikingTeamCount;
    private JLabel jlEndGameLabel;
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(300, 557);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    public TakenPiecesPanel(Board board) {
        this.setBorder(PANEL_BORDER);
        this.setPreferredSize(TAKEN_PIECES_DIMENSION);
        jlSlavsTeam = new JLabel("Count of Slavs warriors: ");
        jlSlavsTeam.setBounds(10, 10, 150, 30);
        this.add(jlSlavsTeam);
        jlVikingTeam = new JLabel("Count of Viking warriors: ");
        jlVikingTeam.setBounds(10, 100, 150, 30);
        this.add(jlVikingTeam);
        jlSlavsTeamCount = new JLabel(String.valueOf(board.slavPlayer().getActivePieces().size()));
        jlSlavsTeamCount.setBounds(160, 10, 100, 30);
        this.add(jlSlavsTeamCount);
        jlVikingTeamCount = new JLabel(String.valueOf(board.vikingPlayer().getActivePieces().size()));
        jlVikingTeamCount.setBounds(160, 100, 100, 30);
        this.add(jlVikingTeamCount);
    }
    // Обновление счетчиков воинов обеих команд
    public void updateCounts(Board board) {
        this.jlSlavsTeamCount.setText(String.valueOf(board.slavPlayer().getActivePieces().size()));
        this.jlVikingTeamCount.setText(String.valueOf(board.vikingPlayer().getActivePieces().size()));
    }
    // Проверка условий завершения игры
    public void checkWinCondition(Board board){
        if (board.checkVikingWinConditions()){
            jlEndGameLabel = new JLabel();
            jlEndGameLabel.setBounds(100, 200, 150, 30);
            this.add(jlEndGameLabel);
            jlEndGameLabel.setText("Vikings win!!!");
        }
        else if (board.checkSlavWinConditions()){
            jlEndGameLabel = new JLabel();
            jlEndGameLabel.setBounds(100, 200, 150, 30);
            this.add(jlEndGameLabel);
            jlEndGameLabel.setText("Slavs win!!!");
        }
    }
}
