/* 
 * Copyright 2008 JRimum Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Created at: 30/03/2008 - 23:49:00
 *
 * ================================================================================
 *
 * Direitos autorais 2008 JRimum Project
 *
 * Licenciado sob a Licença Apache, Versão 2.0 ("LICENÇA"); você não pode 
 * usar esse arquivo exceto em conformidade com a esta LICENÇA. Você pode obter uma 
 * cópia desta LICENÇA em http://www.apache.org/licenses/LICENSE-2.0 A menos que 
 * haja exigência legal ou acordo por escrito, a distribuição de software sob esta 
 * LICENÇA se dará “COMO ESTÁ”, SEM GARANTIAS OU CONDIÇÕES DE QUALQUER TIPO, sejam 
 * expressas ou tácitas. Veja a LICENÇA para a redação específica a reger permissões 
 * e limitações sob esta LICENÇA.
 * 
 * Criado em: 30/03/2008 - 23:49:00
 * 
 */
	
package br.com.nordestefomento.jrimum.utilix;

import static br.com.nordestefomento.jrimum.utilix.ObjectUtil.isNotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.SimpleBookmark;


/**
 * 
 * <p>
 * DEFINIÇÃO DA CLASSE
 * </p>
 * 
 * <p>
 * OBJETIVO/PROPÓSITO
 * </p>
 * 
 * <p>
 * EXEMPLO: 
 * </p>
 * 
 * @author Gilmar P.S.L.
 * 
 * @since 0.2
 * 
 * @version 0.2 
 */

public class PDFUtil {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final Logger LOG = Logger.getLogger(PDFUtil.class);

	public static RectanglePDF changeField2Image(PdfStamper stamper, float[] positions , Image image) throws DocumentException{
						
				RectanglePDF rect =  new RectanglePDF(positions);
				
				return changeField2Image(stamper,rect,image);
	}
	
	public static RectanglePDF changeField2Image(PdfStamper stamper,
			RectanglePDF rect, Image image) throws DocumentException {

		// Ajustando o tamanho da imagem de acordo com o tamanho do campo.
		// image.scaleToFit(rect.getWidth(), rect.getHeight());
		image.scaleAbsolute(rect.width(), rect.height());

		// A rotina abaixo tem por objetivo deixar a imagem posicionada no
		// centro
		// do field, tanto na perspectiva horizontal como na vertical.
		// Caso não se queira mais posicionar a imagem no centro do field, basta
		// efetuar a chamada a seguir:
		// "image.setAbsolutePosition
		// (rect.getLowerLeftX(),rect.getLowerLeftY());"
		image.setAbsolutePosition(rect.getLowerLeftX()
				+ (rect.width() - image.scaledWidth()) / 2, rect
				.getLowerLeftY()
				+ (rect.height() - image.scaledHeight()) / 2);

		// cb = stamper.getUnderContent(rect.getPage());
		stamper.getOverContent(rect.getPage()).addImage(image);

		return rect;
	}
	
	/**
	 * Junta varios arquivos pdf em um soh.
	 * 
	 * @param pdfFiles Lista de array de bytes
	 * 
	 * @return Arquivo PDF em forma de byte
	 */
	@SuppressWarnings("unchecked")
	public static byte[] mergeFiles(List<byte[]> pdfFiles) {

		// retorno
		byte[] bytes = null;

		if (isNotNull(pdfFiles) && !pdfFiles.isEmpty()) {

			int pageOffset = 0;
			boolean first = true;

			ArrayList master = null;
			Document document = null;
			PdfCopy writer = null;
			ByteArrayOutputStream byteOS = null;

			try {

				byteOS = new ByteArrayOutputStream();
				master = new ArrayList();

				for (byte[] doc : pdfFiles) {

					if (isNotNull(doc)) {

						// cria-se um reader para cada documento

						PdfReader reader = new PdfReader(doc);

						if (reader.isEncrypted()) {
							reader = new PdfReader(doc, "".getBytes());
						}

						reader.consolidateNamedDestinations();

						// pega-se o numero total de paginas
						int n = reader.getNumberOfPages();
						List bookmarks = SimpleBookmark.getBookmark(reader);

						if (isNotNull(bookmarks)) {
							if (pageOffset != 0) {
								SimpleBookmark.shiftPageNumbers(bookmarks,
										pageOffset, null);
							}
							master.addAll(bookmarks);
						}

						pageOffset += n;

						if (first) {

							// passo 1: criar um document-object
							document = new Document(reader
									.getPageSizeWithRotation(1));

							// passo 2: criar um writer que observa o documento
							writer = new PdfCopy(document, byteOS);
							document.addAuthor("JRimum Group");
							document.addSubject("JRimum Merged Document");
							document.addCreator("JRimum Utilix");

							// passo 3: abre-se o documento
							document.open();
							first = false;
						}

						// passo 4: adciona-se o conteudo
						PdfImportedPage page;
						for (int i = 0; i < n;) {
							++i;
							page = writer.getImportedPage(reader, i);

							writer.addPage(page);
						}
					}
				}

				if (master.size() > 0) {
					writer.setOutlines(master);
				}

				// passo 5: fecha-se o documento
				if (isNotNull(document)) {
					document.close();
				}

				bytes = byteOS.toByteArray();

			} catch (Exception e) {
				LOG.error("", e);
			}
		}

		return bytes;
	}
}
