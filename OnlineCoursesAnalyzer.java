import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is just a demo for you, please run it on JDK17 (some statements may be not allowed in l
 * ower version).
 * This is just a demo, and you can extend and implement functions
 * based on this demo, or implement it in a different way.
 */
public class OnlineCoursesAnalyzer {

    List<Course> courses = new ArrayList<>();

    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                Course course = new Course(info[0], info[1], new Date(info[2]), info[3], info[4], info[5],
                        Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]),
                        Integer.parseInt(info[9]), Integer.parseInt(info[10]), Double.parseDouble(info[11]),
                        Double.parseDouble(info[12]), Double.parseDouble(info[13]), Double.parseDouble(info[14]),
                        Double.parseDouble(info[15]), Double.parseDouble(info[16]), Double.parseDouble(info[17]),
                        Double.parseDouble(info[18]), Double.parseDouble(info[19]), Double.parseDouble(info[20]),
                        Double.parseDouble(info[21]), Double.parseDouble(info[22]));
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1
    public Map<String, Integer> getPtcpCountByInst() {
        Stream<Course> courseStream = courses.stream();
        Map<String,Integer> a = courseStream.collect(Collectors.groupingBy(Course::getInstitution,
                Collectors.summingInt(Course::getParticipants)));

        return a;
    }

    //2
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        Stream<Course> courseStream = courses.stream();
        Map<String, Integer> a = courseStream.collect(Collectors.groupingBy(Course::q2 , Collectors.summingInt(Course::getParticipants)));
        Map<String, Integer> b = new LinkedHashMap<>();
        a.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> b.put(x.getKey(), x.getValue()));
        return b;
    }


    //3
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Stream<Course> courseStream = courses.stream();
//        List<Course> a= courseStream.filter(course -> course.getInstructors().split(",").length == 1)
//        .toList();
        Map<String,List<String>> a = courseStream.filter(course -> course.getInstructors().
                split(",").length == 1).sorted(Comparator.comparing(Course::getTitle)).collect(Collectors.groupingBy(Course::getInstructors,Collectors.mapping(Course::getTitle,Collectors.toList())));
//        Map<String, List<List<String>>>c= a.forEach((key,value)->b.merge(key,value
//        ,(v1,v2)->{v1,v2}));
        Map<String, List<List<String>>> c = new HashMap<>();
        Iterator<Map.Entry<String,List<String>>> iterator = a.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,List<String>> entry = iterator.next();
            List<List<String>> d = new ArrayList<>();
            List<String> e = new ArrayList<>();
            d.add(entry.getValue().stream().distinct().toList());
            d.add(e);
            c.put(entry.getKey(),d);
        }
        courses.stream().forEach(x -> {
            String[] f = x.getInstructors().split(", ");
            if (f.length != 1) {
                for (int i = 0; i < f.length; i++) {
                    if (c.containsKey(f[i])){
                        boolean bb = false;
                        for (int i1 = 0; i1 < c.get(f[i]).get(1).size(); i1++) {
                            if (c.get(f[i]).get(1).get(i1).equals(x.getTitle())) {
                                bb = true;
                                break;
                            }
                        }
                        if (bb) {
                            continue;
                        }
                        c.get(f[i]).get(1).add(x.getTitle());
                    } else {
                        List<List<String>> g = new ArrayList<>();
                        List<String> h = new ArrayList<>();
                        List<String> j = new ArrayList<>();
                        g.add(h);
                        j.add(x.getTitle());
                        g.add(j);
                        c.put(f[i], g);
                    }
                }
            }
        });
        c.forEach((key, value) -> {
            Collections.sort(value.get(1));
        });
//        StringBuilder sb=new StringBuilder();
//        c.entrySet().stream().forEach(x->{
//                sb.append(x);
//                sb.append("\n");
//        });
//        print_txt(String.valueOf(sb));
//


        return c;
    }

    //4
    public List<String> getCourses(int topK, String by) {
        Stream<Course> courseStream = courses.stream();
        List<String> a = null;
        if (by.equals("hours")) {
            a = courseStream.sorted(Comparator.comparing(Course::getTotalHours).reversed()).
                    distinct().limit(topK).map(Course::getTitle).toList();
        } else if (by.equals("participants")){
            a = courseStream.sorted(Comparator.comparing(Course::getParticipants).reversed()).distinct().limit(topK).map(Course::getTitle).toList();
        }
        return a;
    }

    //5
    public List<String> searchCourses(String courseSubject, double percentAudited, double totalCourseHours) {
        Stream<Course> courseStream = courses.stream();
        List<String> a = courseStream.filter(x -> x.getSubject().toLowerCase(Locale.ROOT).contains(courseSubject.toLowerCase(Locale.ROOT))
         && (x.getPercentAudited() >= percentAudited) && (x.getTotalHours() <= totalCourseHours)).map(Course::getTitle).distinct().sorted().collect(Collectors.toList());
        int b=0;
        return a;
    }

    //6
//    public List<String> recommendCourses1(int age, int gender, int isBachelorOrHigher) {
//        Stream<Course>courseStream=courses.stream();
//        Map<String,List<Course>>a=courseStream.sorted(Comparator.comparing(x -> x.launchDate)).collect(Collectors.groupingBy(Course::getNumber,Collectors.toList()));
//        List<String>b= a.entrySet().stream().sorted((x,y)->{
//            double x_median_age=0;
//            double y_median_age=0;
//            double x_male=0;
//            double y_male=0;
//            double x_bdh=0;
//            double y_bdh=0;
//            List<Course> c=x.getValue();
//                    for (int i = 0; i < c.size(); i++) {
//                        Course d=c.get(i);
//                        x_median_age+=d.medianAge;
//                        x_male+=d.percentMale;
//                        x_bdh+=d.percentDegree;
//                    }
//                    x_median_age/=c.size();
//                    x_male/=c.size();
//                    x_bdh/=c.size();
//            List<Course> e=y.getValue();
//            for (int i = 0; i < e.size(); i++) {
//                Course f=e.get(i);
//                y_median_age+=f.medianAge;
//                y_male+=f.percentMale;
//                y_bdh+=f.percentDegree;
//            }
//            y_median_age/=e.size();
//            y_male/=e.size();
//            y_bdh/=e.size();
//               return  (int) (Math.pow(age-x_median_age,2)+Math.pow(gender*100-x_male,2)-Math.pow(isBachelorOrHigher*100-x_bdh,2)
//               -(Math.pow(age-y_median_age,2)+Math.pow(gender*100-y_male,2)-Math.pow(isBachelorOrHigher*100-y_bdh,2)
//               ));}
//        ).map(x->x.getValue().get(x.getValue().size()-1).getTitle()).collect(Collectors.toList());
//        Collections.reverse(b);
//        b=b.stream().limit(10).collect(Collectors.toList());
//        int m=0;
//
//
//        return null;
//    }

    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher){
        Stream<Course> courseStream = courses.stream();
        Map<String,List<Course>> a = courseStream.sorted(Comparator.comparing(x -> x.launchDate)).collect(Collectors.groupingBy(Course::getNumber,Collectors.toList()));
        Map<String,Double> b = new HashMap<>();
        a.entrySet().stream().forEach(x -> {
            double x_median_age = 0;
            double x_male = 0;
            double x_bdh = 0;
            List<Course> c = x.getValue();
            for (int i = 0; i < c.size(); i++) {
                Course d = c.get(i);
                x_median_age += d.medianAge;
                x_male += d.percentMale;
                x_bdh += d.percentDegree;
            }
            x_median_age /= c.size();
            x_male /= c.size();
            x_bdh /= c.size();
            double sim = Math.pow(age-x_median_age,2) + Math.pow(gender * 100 - x_male, 2) + Math.pow(isBachelorOrHigher * 100-x_bdh, 2);
            b.put(x.getValue().get(x.getValue().size()-1).getTitle(),sim);
        });
        List c = b.entrySet().stream().sorted((x,y) -> (int) (x.getValue() - y.getValue())).map(x -> x.getKey()).collect(Collectors.toList());
        c = c.stream().limit(10).toList();

        StringBuilder sb = new StringBuilder();
        c.stream().forEach(x -> {
                sb.append(x);
                sb.append("\n");
        });

        print_txt(String.valueOf(sb));

        return c;
    }



    public void print_txt(String s){
        String filePath = "E:\\java 2\\a1\\Java2-A1-Release\\resources\\check.txt";
        String content = s;
        FileWriter fw = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter(filePath);
            fw.write(content);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fw.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }


    }

}

class Course {
    String institution;
    String number;
    Date launchDate;
    String title;
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;
    @Override
    public boolean equals(Object obj) {
        Course c = (Course) obj;
        return this.getTitle().equals(c.getTitle());
    }
    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public Course(String institution, String number, Date launchDate,
                  String title, String instructors, String subject,
                  int year, int honorCode, int participants,
                  int audited, int certified, double percentAudited,
                  double percentCertified, double percentCertified50,
                  double percentVideo, double percentForum, double gradeHigherZero,
                  double totalHours, double medianHoursCertification,
                  double medianAge, double percentMale, double percentFemale,
                  double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) title = title.substring(1);
        if (title.endsWith("\"")) title = title.substring(0, title.length() - 1);
        this.title = title;
        if (instructors.startsWith("\"")) instructors = instructors.substring(1);
        if (instructors.endsWith("\"")) instructors = instructors.substring(0, instructors.length() - 1);
        this.instructors = instructors;
        if (subject.startsWith("\"")) subject = subject.substring(1);
        if (subject.endsWith("\"")) subject = subject.substring(0, subject.length() - 1);
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;


    }

    public String getInstitution() {
        return institution;
    }

    public int getParticipants() {
        return participants;
    }

    public String getSubject() {
        return subject;
    }
    public String q2(){
        return this.getInstitution()+"-"+this.getSubject();
    }

    public String getInstructors() {
        return instructors;
    }

    public String getTitle() {
        return title;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public double getPercentAudited() {
        return percentAudited;
    }

    public double getMedianAge() {
        return medianAge;
    }

    public double getPercentMale() {
        return percentMale;
    }

    public double getPercentDegree() {
        return percentDegree;
    }

    public String getNumber() {
        return number;
    }
}
