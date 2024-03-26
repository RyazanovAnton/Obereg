package OberegGUI;

import OberegEngine.Board.Board;

import javax.swing.*;
import java.awt.*;
// Область экрана, на которой отображаются данные по количеству
// воинов обеих команд и результат игры
public class GameInfoPanel extends JLabel {
    private ImageIcon gameStatus = new ImageIcon("./art/currentplayer2.png");
    private ImageIcon backToMenu = new ImageIcon("./art/backTable.png");
    private ImageIcon chainTableVik = new ImageIcon("./art/chaintablvik.png");
    private  ImageIcon chainTableSl = new ImageIcon("./art/chaintablesl.png");
    private  ImageIcon vikPlayer = new ImageIcon("art/pieces/plain/viklabel.png");
    private  ImageIcon slavPlayer = new ImageIcon("art/pieces/plain/sllabel.png");
    private  Font mainFont = new Font("Blackburr", Font.PLAIN, 50);
    private Font countsFont = new Font("Blackburr", Font.PLAIN, 100);
    private  Color vikTeamColor = new Color(123, 11, 11);
    private   Color slavTeamColor = new Color(11, 25, 147);
    private JLabel jlSlavsTeamInfo;
    private  JLabel jlVikingsTeamInfo;
    private  JLabel jlCountsOfSlavs;
    private JLabel jlCountsOfVikings;
    private  JLabel jlChainSl;
    private  JLabel jlChainVik;
    private JLabel jlGameStatus;
    private  JLabel jlVikPlayerIcon;
    private  JLabel jlSlavPlayerIcon;
    private  JLabel jlGameStatusLogo;

    private JLabel jlBackToMenu;
    Rectangle frameSize = new Rectangle(0,0,1294,755);


    public GameInfoPanel(Board board){
        this.setLayout(null);
        this.setBounds(new Rectangle());
        this.setBounds(frameSize);

        // Отрисовка статистики первого игрока
        jlSlavsTeamInfo = new JLabel("Slavs team");
        jlSlavsTeamInfo.setBounds(45, 72, 250, 50);
        jlSlavsTeamInfo.setFont(mainFont);
        jlSlavsTeamInfo.setForeground(slavTeamColor);
        this.add(jlSlavsTeamInfo);
        jlChainSl = new JLabel(chainTableSl);
        jlChainSl.setBounds(6,0, 272, 140);
        this.add(jlChainSl);
        // Получение информации / отрисовка оставшихся фигур первого игрока
        jlCountsOfSlavs = new JLabel(String.valueOf(board.slavPlayer().getActivePieces().size()));
        jlCountsOfSlavs.setBounds(120,150, 100,100);
        jlCountsOfSlavs.setFont(countsFont);
        jlCountsOfSlavs.setForeground(slavTeamColor);
        this.add(jlCountsOfSlavs);

        // Отрисовка информации второго игрока
        jlVikingsTeamInfo = new JLabel("Vikings team");
        jlVikingsTeamInfo.setBounds(1024, 72, 250, 50);
        jlVikingsTeamInfo.setFont(mainFont);
        jlVikingsTeamInfo.setForeground(vikTeamColor);
        this.add(jlVikingsTeamInfo);
        jlChainVik = new JLabel(chainTableSl);
        jlChainVik.setBounds(1002,0, 272, 140);
        this.add(jlChainVik);
        // Получение информации / отрисовка оставшихся фигур второго игрока
        jlCountsOfVikings = new JLabel(String.valueOf(board.vikingPlayer().getActivePieces().size()));
        jlCountsOfVikings.setBounds(1110,150, 100,100);
        jlCountsOfVikings.setFont(countsFont);
        jlCountsOfVikings.setForeground(vikTeamColor);
        this.add(jlCountsOfVikings);

        // Область игрового поля, где отрисовывается информация о состоянии игры и текущем ходе
        jlGameStatus = new JLabel("Current move:");
        jlGameStatus.setBounds(1002, 460, 275, 70);
        jlGameStatus.setFont(mainFont);
        jlGameStatus.setForeground(vikTeamColor);
        jlGameStatus.setHorizontalAlignment(CENTER);
        jlGameStatus.setVerticalAlignment(TOP);
        this.add(jlGameStatus);
        jlVikPlayerIcon = new JLabel(vikPlayer);
        jlVikPlayerIcon.setBounds(1002,595,275,130);
        jlVikPlayerIcon.setHorizontalAlignment(CENTER);
        jlVikPlayerIcon.setVerticalAlignment(TOP);
        this.add(jlVikPlayerIcon);
        jlSlavPlayerIcon = new JLabel(slavPlayer);
        jlSlavPlayerIcon.setBounds(1002,595,275,130);
        jlSlavPlayerIcon.setHorizontalAlignment(CENTER);
        jlSlavPlayerIcon.setVerticalAlignment(TOP);
        jlSlavPlayerIcon.setVisible(false);
        this.add(jlSlavPlayerIcon);
        jlGameStatusLogo = new JLabel(gameStatus);
        jlGameStatusLogo.setBounds(1002,410,275,310);
        this.add(jlGameStatusLogo);
        // Кнопка завершения игры, возврата в главное меню
        jlBackToMenu = new JLabel(backToMenu);
        jlBackToMenu.setBounds(2,500,275,250);
        this.add(jlBackToMenu);
    }
    // Обновление счетчиков воинов обеих команд
    public void updateCounts(Board board) {
        this.jlCountsOfSlavs.setText(String.valueOf(board.slavPlayer().getActivePieces().size()));
        this.jlCountsOfVikings.setText(String.valueOf(board.vikingPlayer().getActivePieces().size()));
    }
    // Метод для определения текущего владельца хода и отрисовки этой информации на панели
    public void currentMove(Board board){
        if(board.currentPlayer().getAlliance().isSlavs()){
            jlGameStatus.setForeground(slavTeamColor);
            jlVikPlayerIcon.setVisible(false);
            jlSlavPlayerIcon.setVisible(true);
        } else {
            jlGameStatus.setForeground(vikTeamColor);
            jlVikPlayerIcon.setVisible(true);
            jlSlavPlayerIcon.setVisible(false);
        }
    }

    // Проверка условий завершения игры
    public void checkWinCondition(Board board){
        if (board.checkVikingWinConditions()){
            jlGameStatus.setForeground(vikTeamColor);
            jlGameStatus.setText("Vikings win!!!");
        }
        else if (board.checkSlavWinConditions()){
            jlGameStatus.setForeground(slavTeamColor);
            jlGameStatus.setText("Slavs win!!!");
        }
    }
}
