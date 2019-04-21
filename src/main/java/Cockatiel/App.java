package Cockatiel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class App
{
    private static final String STUDENT_INFO_FILE_PATH = "Student Info.xlsx";
    private static final String TEST_SCORES_FILE_PATH = "Test Scores.xlsx";
    private static final String TEST_RETAKE_SCORES_FILE_PATH = "Test Retake Scores.xlsx";

    private static void readFileIntoList(Map<Double, Student> studentList, String filePath) {
        // Read in data as is from files
        try {
            DataFormatter dataFormatter = new DataFormatter();
            Workbook workbook = WorkbookFactory.create(new File(filePath));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            if(rowIterator.hasNext()) { rowIterator.next(); } // Skip first line with column names
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Double studentId = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(0)));

                switch(filePath) {
                    case STUDENT_INFO_FILE_PATH: {
                        String major = dataFormatter.formatCellValue(row.getCell(1));
                        char gender = dataFormatter.formatCellValue(row.getCell(2)).toCharArray()[0];
                        studentList.put(studentId, new Student(studentId, major, gender));
                        break;
                    }
                    case TEST_SCORES_FILE_PATH: {
                        double score = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(1)));
                        studentList.get(studentId).setScoreOriginal(score);
                        break;
                    }
                    case TEST_RETAKE_SCORES_FILE_PATH: {
                        double score = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(1)));
                        studentList.get(studentId).setScoreRetake(score);
                        break;
                    }
                    default:
                        break;
                }
            }
        } catch (InvalidFormatException | IOException ex) {
            System.out.println( "File Exception: " + ex.getMessage() );
        }
    }

    public static void main( String[] args )
    {
        Map<Double, Student> studentList = new HashMap<>();
        readFileIntoList(studentList, STUDENT_INFO_FILE_PATH);
        readFileIntoList(studentList, TEST_SCORES_FILE_PATH);
        readFileIntoList(studentList, TEST_RETAKE_SCORES_FILE_PATH);

        // Calculate average score
        double avgScore = 0;
        for (Student student: studentList.values()) {
            avgScore += student.scoreOriginal > student.scoreRetake ? student.scoreOriginal : student.scoreRetake;
        }
        avgScore = avgScore / studentList.size();

        // Find female CS majors
        TreeSet<Integer> femaleCSMajors = new TreeSet<>();
        for (Student student: studentList.values()) {
            if(student.gender == 'F' && student.major.equals("computer science")) {
                femaleCSMajors.add(student.id.intValue());
            }
        }

        Integer[] result = femaleCSMajors.toArray(new Integer[0]);
        HttpRequest.executePost("http://3.86.140.38:5000/challenge", "stephenaker1@gmail.com", "Stephen Akers", (int)avgScore, result);

    }
}
