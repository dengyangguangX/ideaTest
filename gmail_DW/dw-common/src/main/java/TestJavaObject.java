import java.util.Map;

class Student implements Cloneable{
    private int number;
    private Map<String,Integer> map;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public Object clone() {
        Student stu = null;
        try{
            stu = (Student)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
public class TestJavaObject {
    public static void main(String args[]) {
//        Student stu1 = new Student();
//        stu1.setNumber(12345);
//        Student stu2 = (Student)stu1.clone();
//
//        System.out.println("学生1:" + stu1.getNumber());
//        System.out.println("学生2:" + stu2.getNumber());
//
//        stu2.setNumber(54321);
//
//        System.out.println("学生1:" + stu1.getNumber());
//        System.out.println("学生2:" + stu2.getNumber());

        String[] strings = {"1", "3"};

    }
}  