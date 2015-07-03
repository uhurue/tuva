package fireeye;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

class ReportInfo
{
    String reportId;
    HashMap<String, Integer> elementMap;
    public String getReportId()
    {
        return reportId;
    }
    public void setReportId(String reportId)
    {
        this.reportId = reportId;
    }
    public HashMap<String, Integer> getElementMap()
    {
        return elementMap;
    }
    public void setElementMap(HashMap<String, Integer> elementMap)
    {
        this.elementMap = elementMap;
    }
    
}

class TermSet
{
    int termCount;
    HashMap<String, TermData> terms;
    public int getTermCount()
    {
        return termCount;
    }
    public void setTermCount(int termCount)
    {
        this.termCount = termCount;
    }
    public HashMap<String, TermData> getTerms()
    {
        return terms;
    }
    public void setTerms(HashMap<String, TermData> terms)
    {
        this.terms = terms;
    }
    
}
class TermData
{
    String name;
    ArrayList<String> attrMode;
    ArrayList<String> attrEtc;
    int count;

    public TermData(String name, int count)
    {
        this.setName(name);
        this.setCount(count);
    }
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public ArrayList<String> getAttrMode()
    {
        return attrMode;
    }
    public void setAttrMode(ArrayList<String> attrMode)
    {
        this.attrMode = attrMode;
    }
    public ArrayList<String> getAttrEtc()
    {
        return attrEtc;
    }
    public void setAttrEtc(ArrayList<String> attrEtc)
    {
        this.attrEtc = attrEtc;
    }
    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }
}

public class Report
{
    public static void analysisReport()
    {
        File path = new File(".");
        //path = new File("C:\\Users\\ykkim\\Desktop\\IOC\\FireEye_MaliciousReport_지사수집_정상");
        File [] files = path.listFiles();
        String id="", ext="";
        Report me = new Report();
        ArrayList<ReportInfo> reportlist = new ArrayList<ReportInfo>();
        
        for(File file: files)
        {
            String temp [] = file.getName().split("\\.", 2);
            if(temp.length == 2)
            {
                id = temp[0];
                ext = temp[1];
                if(ext.equals("xml"))
                {
                    reportlist.add(me.processXmlDocument(id, file.getPath()));
                }
            }
        }

        //용어 통계를 내고 용어의 attribute를 조사한다.
        TermSet termSet = new TermSet();
        HashMap<String, TermData> terms = new HashMap<String, TermData>();
        TermData termData;
        termSet.setTermCount(0);
        termSet.setTerms(terms);
        for(ReportInfo report: reportlist)
        {
            System.out.println("id : " + report.getReportId());
            HashMap<String, Integer> elements = report.getElementMap();
            for(String key: elements.keySet())
            {
                if(termSet.getTerms().get(key)==null) //없는 term
                {
                    System.out.println("new term: " + key);
                    termSet.setTermCount(termSet.getTermCount()+1);
                    termData = new TermData(key, elements.get(key)); //레퍼런스 카운트 저장
                    //attribute 조사
                }
                else
                {
                    termData = termSet.getTerms().get(key);
                    termData.setCount(termData.getCount()+elements.get(key)); //카운트를 더한다.
                    System.out.println("cur term: " + key + " / count: " + termData.getCount());
                }
                terms.put(key, termData);
            }
        }
        printTermInfo(termSet, "termInfo.csv");
        printReportInfo(reportlist, new ArrayList<String>(termSet.getTerms().keySet()), "reportInfo.csv");
    }
    public static void main(String[] args)
    {
        //analysisReport();
        testGetElement();
    }
    
    public static void printTermInfo(TermSet termSet, String file)
    {
        BufferedWriter report;
        TermData termData;
        try
        {
            report = new BufferedWriter(new FileWriter(file));
                    
            report.write(String.format("Total term count: %d,\n", termSet.getTermCount()));
            report.write(String.format("termName,count\n", termSet.getTermCount()));
            for(String key: termSet.getTerms().keySet())
            {
                termData = termSet.getTerms().get(key);
                report.write(String.format("%s,%d\n", termData.getName(), termData.getCount()));
            }
            report.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void printReportInfo(ArrayList<ReportInfo> reportlist, ArrayList<String> headers, String file)
    {
        BufferedWriter report;
        String strHeader="report ID";
        String strData="";
        HashMap<String, Integer> elements;
        HashMap<String, Integer> counter = new HashMap<String, Integer>(); //term 별 참조카운트
        
        try
        {
            report = new BufferedWriter(new FileWriter(file));
                    
            report.write(String.format("Total report count: %d,,\n", reportlist.size()));
            for(String hdr: headers)
            {
                strHeader += ("," + hdr);
                counter.put(hdr, 0); //카운터 초기화
            }
            report.write(String.format("%s\n", strHeader));
            
            Integer num = 0;
            for(ReportInfo rpt: reportlist)
            {
                strData=rpt.getReportId();
                elements = rpt.getElementMap();
                for(String key: headers)
                {
                    if(elements.get(key)!=null)
                    {
                        num = elements.get(key);
                    }
                    else //이 term을 쓰지 않은 report
                    {
                        num = 0;
                    } 
                    strData += ("," + num);
                    counter.put(key, counter.get(key)+num);
                }
                report.write(String.format("%s\n", strData));
            }
            strData = "SUM";
            for(String key: headers)
            {
                strData += ("," + counter.get(key));
            }
            report.write(String.format("%s\n", strData));
            report.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public ReportInfo processXmlDocument(String id, String file)
    {
        ReportInfo report = new ReportInfo();
        report.setReportId(id);
        report.setElementMap(getElement(file, "os_change"));
        return report;        

    }
    public HashMap<String, Integer> getElement(String file, String parentElement)
    {
        Document document;
        XPath xpath;
        NodeList cols;
        String expression = "//" + parentElement + "/*";
        HashMap<String, Integer> elementMap = new HashMap<String, Integer>();
        InputSource is;
        String element;
        int i;

        try
        {
            is = new InputSource(new FileReader(file));
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            xpath = XPathFactory.newInstance().newXPath();
            cols = (NodeList)xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
            for(i=0; i<cols.getLength(); i++)
            {
                element = cols.item(i).getNodeName();
                //System.out.println("element : " + element);
                if(elementMap.get(element)==null)
                {
                    elementMap.put(element, 1);
                }
                else
                {
                    elementMap.put(element, elementMap.get(element)+1);
                }
            }
        }
        catch(FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch(SAXException | IOException | ParserConfigurationException e1)
        {
            e1.printStackTrace();
        }
        catch(XPathExpressionException e)
        {
            e.printStackTrace();
        }
        return elementMap;
    }
    
    public static void testGetElement()
    {
        Report me = new Report();
        String file = "C:\\Users\\ykkim\\Desktop\\IOC\\FireEye_MaliciousReport_지사샘플\\264.xml";
        String element = "signature_name";

        HashMap<String, Integer> elementMap = me.getElement(file, element);
        for(String key: elementMap.keySet())
        {
            System.out.println(key + ": " + elementMap.get(key));
        }
    }
}
