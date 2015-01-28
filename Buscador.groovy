import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL
frame = new SwingBuilder().frame(title: 'Proyecto buscador',layout: new BL()){
    panel(layout: new BL(),constraints: BL.NORTH){
        button(id:'btnbuscar', 'Buscar',constraints: BL.WEST,actionPerformed: {
            tAbuscar.text=""
            def get = new Get(url:"http://search.yahoo.com/search" )
            def buscar=txtbuscar.text.replaceAll(" ","%32")
            get.queryString= "p="+buscar
            def principal="", etiqueta_a="",href_body="",href=[], nombre=[]
            def mt = get.text =~ /<ol.*ol>/
            while (mt.find())
                principal = principal + mt.group()
            def mt1 = principal =~ /<(a).*?<\/\1>/
            while (mt1.find())
                etiqueta_a= etiqueta_a+mt1.group()+"\n"
            def mt2 = etiqueta_a =~ /href.*>(.*)?[^(Cached)|^(<\/a;?>)]\w*<\/a>/
            while(mt2.find())
                href_body=href_body+mt2.group()+"\n"
            def mt3 = href_body =~ /(href=".*")/
            while(mt3.find())
                href<<mt3.group()
            def mt4 = href_body =~ />(.*)?\w*<\/a>/
            while(mt4.find())
                nombre<<mt4.group()
            for(i in 0..href.size()-1){
                href[i]=href[i].toString().replaceAll('href="',"")
                href[i]=href[i].toString().substring(0,href[i].toString().indexOf('"'))
                nombre[i]=nombre[i].toString().replaceAll("<b>","").replaceAll("<wbr />","").replaceAll("</a>","")
                        .replaceAll("</b>","").replaceAll(">","")
                tAbuscar.text+= "${nombre[i].toString().padLeft(60)} = ${href[i]}\n"
            }
        })
        textField('',columns: 10,constraints: BL.CENTER,id:'txtbuscar')
    }
    scrollPane(){
        textArea(id: 'tAbuscar',columns: 50,rows: 20,constraints: BL.CENTER,editable: false)
    }
}
frame.pack()
frame.show()
