/**
 * 
 */
package net.bncf.uol2010.utility.navigator;

import org.apache.log4j.Logger;

/**
 * @author massi
 *
 */
public abstract class Navigatore {

	private Logger log = Logger.getLogger(Navigatore.class);
	/**
	 * Questa Variabile viene utilizzata per indicare la posizione delle immagini per il navigatore
	 */
	private String pathImgNav = ".\\images\\";

	/**
	 * In questa variabile vengono indicati il numero di record da visualizzate per ogni pagina
	 */
	private int numRec = 0;

	/**
	 * In quesa variabile viene indicato il numero di pagine da visualizzare alla volta
	 */
	private int numPag = 0;

	/**
	 * In questa variabile vengono indicati il numero totale dei record da visualizzare
	 */
	private int recTot =0;

	/**
	 * In questa variabile viene indicato il numero del record di partenza della pagia da visualizzare
	 */
	private int recIni = 0;

	/**
	 * In questa variabile viene indicato il numero del record di fine della pagina da visualizzare
	 */
	private int recFin = 0;

	/**
	 * In questa variabile viene indicato il numero delle pagine totale da visualizzare
	 */
	private int pagTot = 0;

	/**
	 * In questa variabile viene indicato il numero della pagina attuale visualizzata
	 */
	private int pagNum =1;

	/*
	 * In questa variabile viene indicato il numero della pagini di inizio da visualizzare nel navigatore
	 */
	private int pagIni =1;

	/*
	 * In questa variabile viene indicato il numero della pagini di fine da visualizzare nel navigatore
	 */
	private int pagFin = 0;

	/**
	 * Costruttore della classe
	 *
	 */
	public Navigatore()
	{
	}

	/**
	 * Questo metodo serve per l'estrazione dei dati necessati per il navigatore nel formato QueryString
	 *
	 */
	public abstract String getQueryString();

  /**
   * Questo metodo viene utilizzato per aggiungere una voce nella QueryString
   * @param ris Valore attuale dell QueryString
   * @param name Nome del campo
   * @param value Valore del campo
   * @return Nuovo Valore
   */
  protected String addQueryString(String ris, String name, int value)
  {
    ris += value == 0 ?"":(ris.equals("")?"":"&")+name+"="+value;
    return ris;
  }

  /**
   * Questo metodo viene utilizzato per aggiungere una voce nella QueryString
   * @param ris Valore attuale dell QueryString
   * @param name Nome del campo
   * @param value Valore del campo
   * @return Nuovo Valore
   */
  protected String addQueryString(String ris, String name, String value)
  {
    ris += value.equals("")?"":(ris.equals("")?"":"&")+name+"="+value;
    return ris;
  }

  /**
	 * Questo metodo serve per l'estrazione dei dati necessati per il navigatore nel formato Form HIDDEN
	 *
	 */
	public abstract String getHidden();

  /**
   * Questo metodo viene utilizzato per aggiungere una voce nella Hidden
   * @param ris Valore attuale del Hidden
   * @param name Nome del campo
   * @param value Valore del campo
   * @return Nuovo Valore
   */
  protected String addHidden(String ris, String name, int value)
  {
    ris += value==0?"":"<input type=\"Hidden\" name=\""+name+"\" value=\""+value+"\">";
    return ris;
  }

  /**
   * Questo metodo viene utilizzato per aggiungere una voce nella Hidden
   * @param ris Valore attuale del Hidden
   * @param name Nome del campo
   * @param value Valore del campo
   * @return Nuovo Valore
   */
  protected String addHidden(String ris, String name, String value)
  {
    ris += value.equals("")?"":"<input type=\"Hidden\" name=\""+name+"\" value=\""+value+"\">";
    return ris;
  }

  /**
	 * Questo metodo viene utilizzato per calcolare il record di inizio, fine e il numero di pagine totali e fine
	 *
	 */
	protected void calcNum()
	{
		try
		{
			recIni = ((pagNum-1)*numRec)+1;
			recFin = recIni+numRec-1;
			if (recFin>recTot)
			{
				recFin = recTot;
			}
			pagTot = recTot/numRec;
			if ((pagTot*numRec)<recTot)
			{
				pagTot++;
			}
			pagFin = (pagIni+numPag)-1;
			if (pagFin>pagTot)
			{
				pagFin=pagTot;
			}
		}
		catch (ArithmeticException exc)
		{
		}
	}

  public String viewNavigatore(String myQueryString, String myHiddenField)
  {
  	String ris = "";

    ris += "<table border=0 cellpadding=\"0\" cellspacing=\"0\" vspace=\"0\" hspace=\"0\" width=\"100%\">\n";
    ris += "<TR>\n";
		ris += "<TD class=\"testo\" width=\"27\" nowrap valign=\"top\">\n";
		ris += "Pagine:\n";
		ris += "</TD>\n";



		for (int x=pagIni; x<=pagFin;x++)
		{
			if (x == pagNum)
			{
				ris += "<TD class=\"testo\"  width=\"15\" valign=\"top\">\n";
				ris+= "<B>";
				ris +=Integer.toString(x);
				ris += "</B>\n";
				ris += "</TD>\n";
			}
			else
			{
				ris += "<Form Method=\"GET\">\n";
				ris += "<TD class=\"testo\"  width=\"15\" valign=\"top\">\n";
				ris += "<input type=\"Hidden\" name=\"pagNum\" value=\""+Integer.toString(x)+"\">";
				ris += "<input type=\"Hidden\" name=\"pagIni\" value=\""+Integer.toString(pagIni)+"\">";
				ris += myHiddenField;
				ris += "<Input type=submit name=\"m1\" value=\""+Integer.toString(x)+"\" class=button2>\n";
				ris += "</TD>\n";
				ris +="</Form>\n";
			}

	}
  	ris += "<TD class=\"testo\" width=\"10\" valign=\"top\"><B>/</B></TD>";
		ris += "<Form Method=\"GET\">\n";
		ris += "<TD class=\"testo\"  width=\"15\">\n<B>";
		ris += "<input type=\"Hidden\" name=\"pagNum\" value=\""+Integer.toString(pagTot)+"\">";
		ris += "<input type=\"Hidden\" name=\"pagIni\" value=\""+Integer.toString(pagTot)+"\">";
		ris += myHiddenField;
		ris += "<Input type=submit name=\"m1\" value=\""+Integer.toString(pagTot)+"\" class=button2>\n";
		ris += "</TD>\n";
		ris +="</Form>\n";
//  	ris +="</B></TD>";
    ris += "<TD valign=\"top\">";
		if (pagIni>1)
		{
			if (pagIni-numPag<1)
				ris += "<A Href=\"?TipopagNum="+pagNum+"&pagIni=1";
			else
				ris += "<A Href=\"?TipopagNum="+pagNum+"&pagIni="+(pagIni-numPag);
			if (!myQueryString.equals(""))
			{
				ris += "&";
				ris += myQueryString;
			}
			ris += "\">";
			ris += "<Img src=\""+pathImgNav+"indietro.gif\" border=\"0\" width=\"14\" height=\"14\" align=\"middle\">";
			ris += "</A>";
		}
		else
		{
			ris += "<Img src=\""+pathImgNav+"indietro.gif\" border=\"0\" width=\"14\" height=\"14\" align=\"middle\">";
		}

		if (pagFin<pagTot)
		{
			ris += "<A Href=\"?TipopagNum="+pagNum+"&pagIni="+(pagFin+1);
			if (!myQueryString.equals(""))
			{
				ris += "&";
				ris += myQueryString;
			}
			ris += "\">";
			ris += "<Img src=\""+pathImgNav+"avanti.gif\" border=\"0\" width=\"14\" height=\"14\" align=\"middle\">";
			ris += "</A>";
		}
		else
		{
			ris += "<Img src=\""+pathImgNav+"avanti.gif\" border=\"0\" width=\"14\" height=\"14\" align=\"middle\">";
		}
		ris += "</TD>";

		ris += "<TD class=\"testo\" align='right' valign=\"top\">record visualizzati&nbsp;<B>"+recIni+"-"+recFin+"/"+recTot+"</B></TD>";
    ris += "</TR>";
    ris += "</Table>";

  	return ris;
  }

	/**
	 * Questo metodo viene utilizzato per indicare il numero della paggina che attualmente viene visualizzata
	 * @param i
	 */
	public void setPagNum(int i)
	{
		pagNum= i;
	}

	/**
	 * Questo metodo viene utilizzato per indicare il numero di partenza della pagina da visualizzare nel navigatore
	 * @param i
	 */
	public void setPagIni(int i)
	{
		pagIni = i;
	}

	/**
	 * Questo metodo viene utilizzato per indicare il nume totale di record di cui � composta la ricerca
	 * @param i
	 */
	protected void setRecTot(int i)
	{
		recTot = i;
	}

	/**
	 * Questo metodo viene uitilizzato per indicare il numero di paggine che devono essere visualizzate alla volta
	 * @param i
	 */
	public void setNumPag(int i)
	{
		numPag = i;
	}

	/**
	 * Questo metodo viene utilizzato per indicare il numero di record che devono essere visualizzate alla volta
	 * @param i
	 */
	public void setNumRec(int i)
	{
		numRec = i;
	}

	/**
	 * Questo metodo viene utilizzato per visualizzare il numero del record di fine della visualizzazione
	 *
	 */
	public int getRecFin()
	{
		return recFin;
	}

	/**
	 * Questo metodo viene utilizzato per visualizzare il numero di record di partenza della visualizzazione
	 *
	 */
	public int getRecIni()
	{
		return recIni;
	}

	/**
	 * Tramite questo metodo viene letto il contenuto della variabile pathImgNav che indica la path dove si trovano
	 * le imamgini per il navigatore (Default: .\images\)
	 *
	 */
	public String getPathImgNav()
	{
		return pathImgNav;
	}

	/**
	 * Tramite questo metodo viene valorizzato il contenuto della variabile pathImgNav che indica la path dove si
	 * trovano le imamgini per il navigatore (Default: .\images\)
	 * @param string
	 */
	public void setPathImgNav(String string)
	{
		pathImgNav = string;
	}

	/**
	 * Tramite questo metodo si possono sapere il numero totele dei record presenti nella Select
	 *
	 */
	public int getRecTot()
	{
		return recTot;
	}
//
//	/**
//	 * Questo metodo viene utilizzato per estrarre il numero totali di un recordset
//	 *
//	 * @param myRs RecordSet dal quale estrarre il numerodi record
//	 * @return Numero di record trovati
//	 * @throws SQLException
//	 */
//	protected int recCount(ResultSet myRs) throws SQLException
//	{
//		int tot = 0;
//		try
//		{
//			myRs.last();
//			tot = myRs.getRow();
//			myRs.first();
//		}
//		catch (SQLException e)
//		{
//			log.error(e);
//			throw e;
//		}
//		return tot;
//	}
}
