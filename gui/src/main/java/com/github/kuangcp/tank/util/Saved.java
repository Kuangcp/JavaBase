package com.github.kuangcp.tank.util;

import com.github.kuangcp.tank.domain.Brick;
import com.github.kuangcp.tank.domain.EnemyTank;
import com.github.kuangcp.tank.domain.Hero;
import com.github.kuangcp.tank.domain.Iron;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 用文件系统实现保存数据的功能，真繁琐
 * 这里使用了上课时老师教的那种方法，三重流，先字节流然后转换流然后字符流
 * 韩顺平用的是直接字符流 FileReader 和 BufferedReader 就完成了功能
 * 等学会数据库操作，再重新实现这个功能
 * 谨记！！
 * <p>
 * 用数据库实现了续上局及其存盘退出，比文件的操作简单多了。。。
 */
public class Saved {
    private Vector<EnemyTank> ets;
    private Hero hero;
    private Vector<Iron> irons;
    private Vector<Brick> bricks;
    int[][] ETS;
    int[] myself;

    public Saved(Vector<EnemyTank> ets, Hero hero, Vector<Brick> bricks, Vector<Iron> irons, int[][] ETS, int[] myself) {
        this.ets = ets;
        this.hero = hero;
        this.bricks = bricks;
        this.irons = irons;
        this.ETS = ETS;
        this.myself = myself;
    }

    //保存一切的函数
    public void savedAll() {
        BufferedWriter bw = null;
        OutputStream out = null;
        OutputStreamWriter os = null;

        EnemyTank s;
        Brick b;
        Iron r;

        try {
            out = new FileOutputStream("./src/RE/File.txt");
//			同等级包下文件，用于存取，导出的时候才可以用上！
            //尚未解决！！！
//			URL j = (Saved.class.getResource("/RE/File.txt"));
//			out = new FileOutputStream(j.getPath());
            os = new OutputStreamWriter(out);
            bw = new BufferedWriter(os);
            //<>是敌人坦克

            bw.write("<             \r\n");
//			bw.write("写进去了\r\n");
            for (int i = 0; i < ets.size(); i++) {
                if ((s = ets.get(i)) != null && s.isAlive()) {

                    String qi = "", qx = "", qy = "";//i-2,x-3,y-3

                    if (i < 10) qi = i + " ";
                    else qi += i;
                    if (s.getX() < 100) qx = s.getX() + " ";
                    else qx += s.getX();
                    if (s.getY() < 100) qy = s.getY() + " ";
                    else qy += s.getY();

                    bw.write(qi + " " + qx + " " + qy + "\r\n");
                }
            }
            bw.write(">             \r\n");
            //##是自己坦克
            bw.write("#             \r\n");
//			bw.write("#             \r\n");
            String Hx = "", Hy = "", Hl = "", Hp = "";//x-3,y-3,l-2,p-3

            if (hero.getX() < 100) Hx = hero.getX() + " ";
            else Hx += hero.getX();
            if (hero.getY() < 100) Hy = hero.getY() + " ";
            else Hy += hero.getY();
            if (hero.getLife() < 10) Hl = hero.getLife() + " ";
            else Hl += hero.getLife();
            if (hero.getPrize() < 10) Hp = hero.getPrize() + "  ";
            else if (hero.getPrize() < 100) Hp = hero.getPrize() + " ";
            else Hp += hero.getPrize();

            bw.write(Hx + " " + Hy + " " + Hl + " " + Hp + "\r\n");
            bw.write("#             \r\n");
//			//..是砖块
//			bw.write("+\r\n");
//			for(int i=0;i<bricks.size();i++){
//				if((b=bricks.get(i))!=null && b.getLive()){
//					bw.write(i+" "+b.getHx()+" "+b.getHy()+"\r\n");
//				}
//			}
//			bw.write("+\r\n");
//			//--是铁块
//			bw.write("-\r\n");
//			for(int i=0;i<irons.size();i++){
//				if((r=irons.get(i))!=null && r.getLive()){
//					bw.write(i+" "+r.getHx()+" "+r.getHy()+"\r\n");
//				}
//			}
//			bw.write("-\r\n");


            bw.flush();//一定要清除缓存./src/RE/File.txt
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //后打开，先关闭
                out.close();
                os.close();
                bw.close();
                System.out.println("保存数据并且关闭全部输出流");
            } catch (Exception e2) {
                e2.printStackTrace();
                System.out.println("有异常");
            }
        }
    }

    //	public static void main(String []d){
    //用main函数测试后，发现是没有错误的，能够读取到数据
    public void readAll() {
        InputStream input = null;
        BufferedReader br = null;
        InputStreamReader ids = null;
        try {
            input = new FileInputStream("./src/RE/File.txt");
            ids = new InputStreamReader(input);
            br = new BufferedReader(ids);

            String L = "";
            while ((L = br.readLine()) != null) {
//				System.out.print(L+"\r\n");
                //读取敌人信息
                if (L.equals("<             ")) {
                    System.out.println("进入了敌人数据的读取");
                    //读取下一行，以及字符串-》数字转换
                    while ((L = br.readLine()) != null) {
//					int m=0;//数组的行标
                        if (L.equals(">             ")) {
                            System.out.println("退出了敌人信息读取");
                            break;
                        }
//						L=br.readLine();
                        String Di, Dx, Dy;

                        Di = L.substring(0, 2);//(0,2]
                        Dx = L.substring(3, 6);
                        Dy = L.substring(7, 10); //是针对每一行的 只要有一行的长度小于这个10了就会抛越界异常
                        System.out.println(Di + "X:" + Dx + "Y:" + Dy);

                        ETS[StoInt(Di)][0] = StoInt(Dx);
                        ETS[StoInt(Di)][1] = StoInt(Dy);
//						m++;
//						System.out.println("X:"+StoInt(Dx)+"Y:"+StoInt(Dy));
//						Demons et = new Demons(StoInt(Dx),StoInt(Dy),2 ,1);
//						et.SetInfo(hero, ets, bricks, irons);
//						et.setLive(true);
//						ets.add(et);
//						System.out.println("坦克创建成功");
//						Thread t = new Thread(et);
//						t.start();
//						System.out.println(t.isAlive());

                        //这里创建是没有用的，这里的函数栈内存退出后就清除了
                        //虽然是传进来了一个集合的引用，但是那个引用是没有内存空间
                        //所以集合只是一个指针，没有内存空间，要想创建并跑起来，只能在MyPanel3里创建并且开启线程
                        //同样的道理，我的坦克也是如此，也只能用数组来做
                    }
                }
//*************  读取我的坦克信息
                if (L.equals("#             ")) {
                    System.out.println("进入了我的数据读取");
                    while ((L = br.readLine()) != null) {
                        if (L.equals("#             ")) {
                            System.out.println("退出了我的读取");
                            break;
                        }
//						L=br.readLine();
                        String hx, hy, hl, hp;
                        hx = L.substring(0, 3);
                        hy = L.substring(4, 7);
                        hl = L.substring(8, 11);
                        hp = L.substring(11, 14);
                        System.out.println(hx + hy + hl + hp);
//						hero = new Hero(StoInt(hx), StoInt(hy), 3, ets, bricks, irons);
//						hero.setLife(StoInt(hl));
//						hero.setPrize(StoInt(hp));
                        myself[0] = StoInt(hx);
                        myself[1] = StoInt(hy);
                        myself[2] = StoInt(hl);
                        myself[3] = StoInt(hp);
                    }
                }
                //读取砖块
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("读取数据有异常");
        } finally {
            try {
                //后打开，先关闭
                input.close();
                ids.close();
                br.close();
                System.out.println("全部关闭了输入流");

            } catch (Exception e2) {
                e2.printStackTrace();
                System.out.println("有异常");
            }
        }
    }

    /**
     * 将String转换成int
     * 只读取字符串从右边第一个数字开始到左边第一个空格结束读取
     * 例如：" 123 4 5   "---> 5
     */
    public int StoInt(String s) {
        int I = 0;
        int bit;
        bit = 1;
        for (int i = 0; i < s.length(); i++) {//获取的length是实际长度要减一
            if (s.charAt(s.length() - i - 1) != ' ') {
                I += (s.charAt(s.length() - i - 1) - 48) * bit;
                bit *= 10;
            }

        }
        return I;
    }

    //	public static void main(String []e){
    public void readDataBase() {
        PreparedStatement ps = null;
        Connection cn = null;
        ResultSet rs = null;

        try {
            //初始化我们的对象
            //1 加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2 得到连接
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tank?user=myth&password=ad&userUnicode=true&characterEncoding=UTF8");
            //创建一个preparedStatement对象用于发送


//			ps = cn.prepareStatement("create table Demons(x int,y int)");
//			ps.execute();

            //查询表 函数主要功能的实现
            ps = cn.prepareStatement("select * from demons");
            rs = ps.executeQuery();
            System.out.println("X   Y");
            int nums = 0;
            while (rs.next()) {
                ETS[nums][0] = rs.getInt(1);
                ETS[nums][1] = rs.getInt(2);
                nums++;
                System.out.println(rs.getInt(1) + " " + rs.getInt(2));
            }
            rs.close();
            ps.close();
            //关闭上面资源，连接自己坦克的表 读入
            ps = cn.prepareStatement("select * from hero");
            rs = ps.executeQuery();
            rs.next();
            for (int i = 0; i < 4; i++) {
                myself[i] = rs.getInt(i + 1);
            }

//			for (int i=1;i<5;i++){
//				ps=cn.prepareStatement("insert into demons values(?,?)");
//				ps.setInt(1, i*30+30);
//				ps.setInt(2, i*10+50);
//				int j = ps.executeUpdate();
//				if(j==1){
//					System.out.println("成功");
//				}else {
//					System.out.println("失败");
//				}
//			}
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (cn != null)
                    cn.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

    }

    public void saveDataBase() {
        PreparedStatement ps = null;
        Connection cn = null;
        ResultSet rs = null;

        try {
            //初始化我们的对象
            //1 加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2 得到连接
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Tank?user=myth&password=ad&userUnicode=true&characterEncoding=UTF8");
            //创建一个preparedStatement对象用于发送
            EnemyTank s = null;
            //在写入数据之前就要把表中数据全部删除，不然数据就溢出了
            ps = cn.prepareStatement("delete from demons where x>0");
            int deletes = ps.executeUpdate();
            if (deletes > 1) System.out.println("清除表Demons成功");
            else System.out.println("清除表Demons失败");
            if (ps != null) ps.close();
            ps = cn.prepareStatement("delete from hero where x>0");
            int deletess = ps.executeUpdate();
            if (deletess == 1) System.out.println("清除表Hero成功");
            else System.out.println("清除表Hero失败");
            if (ps != null) ps.close();

            for (int i = 0; i < ets.size(); i++) {
                if ((s = ets.get(i)) != null && s.isAlive()) {
                    ps = cn.prepareStatement("insert into demons values(?,?)");
                    ps.setInt(1, s.getX());
                    ps.setInt(2, s.getY());
                    int cou = ps.executeUpdate();
                    if (cou == 1) System.out.println("demons 保存成功");
                    else System.out.println("demons 保存失败");
                }
            }

            if (ps != null) ps.close();
            ps = cn.prepareStatement("insert into hero values(?,?,?,?)");
            ps.setInt(1, hero.getX());
            ps.setInt(2, hero.getY());
            ps.setInt(3, hero.getLife());
            ps.setInt(4, hero.getPrize());
            int hh = ps.executeUpdate();
            if (hh == 1) System.out.println("Hero 保存成功");
            else System.out.println("Hero 保存失败");
            //
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (cn != null)
                    cn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
}


/**
 * （和src同级）相对路径下的 字节转字符文件 读取操作从同等级的包下读取文件
 * 从同等级的包下读取文件
 * 从同等级的包下读取文件
 */
//	public void savedAll(){
	/*public static void main(String []d){
		InputStream input = null;
		BufferedReader br = null;
		InputStreamReader ids =null;
		
		BufferedWriter bw = null;
		OutputStream out = null;
		OutputStreamWriter os = null;
		
		try{
			input = new FileInputStream("File/Cache.txt");
			ids = new InputStreamReader(input);
			br = new BufferedReader(ids);
			
			
			out = new FileOutputStream("File/Cache.txt");
//			out = new FileOutputStream("E:/Game.txt");
			os = new OutputStreamWriter(out);
			bw = new BufferedWriter(os);
			
			String s = "";
			while((s=br.readLine())!=null){
				System.out.print(s+"\r\n");
//				bw.write(" 输出流0000000 ");
			}
//			bw.write(" 输出流738927589 ");
//			bw.flush();//一定要清除缓存
		}catch(Exception e){
				
		}finally {
			try {
					input.close();
					ids.close();
					br.close();
				
				out.close();
				os.close();
				bw.close();
				System.out.println("全部关闭");
				
			} catch (Exception e2) {
				e2.printStackTrace();
				System.out.println("有异常");
				// TODO: handle exception
			}
		}
		
		System.out.println("ko");
	}*/

/**从同等级的包下读取文件*/ //  ./src/RE/GameBegin.wav
//使用了三种流
	/*public static void main(String []d){
		InputStream input = null;
		BufferedReader br = null;
		InputStreamReader ids =null;
		try{
		input = FileReader.class.getResourceAsStream("/File/Cache.txt");
		ids = new InputStreamReader(input);
		br = new BufferedReader(ids);
		
		String s = "";
		while((s=br.readLine())!=null){
			System.out.print(s+"\r\n");
		}
		}catch(Exception e){
			
		}finally {
			try {
				input.close();
				ids.close();
				br.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}*/

