/*
 * Copyright 2008 JRimum Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * Created at: 30/03/2008 - 18:17:00
 * 
 * ================================================================================
 * 
 * Direitos autorais 2008 JRimum Project
 * 
 * Licenciado sob a Licença Apache, Versão 2.0 ("LICENÇA"); você não pode usar
 * esse arquivo exceto em conformidade com a esta LICENÇA. Você pode obter uma
 * cópia desta LICENÇA em http://www.apache.org/licenses/LICENSE-2.0 A menos que
 * haja exigência legal ou acordo por escrito, a distribuição de software sob
 * esta LICENÇA se dará “COMO ESTÁ”, SEM GARANTIAS OU CONDIÇÕES DE QUALQUER
 * TIPO, sejam expressas ou tácitas. Veja a LICENÇA para a redação específica a
 * reger permissões e limitações sob esta LICENÇA.
 * 
 * Criado em: 30/03/2008 - 18:17:00
 * 
 */


package br.com.nordestefomento.jrimum.utilix;

import static br.com.nordestefomento.jrimum.utilix.ObjectUtil.isNotNull;
import org.apache.commons.lang.StringUtils;


/**
 * <p>
 * Preenchedor de caracteres genérico. É utilizado para preencher objetos <code>String</code>,
 * tanto da esquerda para a direita como da direita para esquerda, com o objeto genérico até
 * o tamanho especificado. Caso o tamanho especificado seja <strong>menor</strong> 
 * ou <strong>igual</strong> a 0 (ZERO), este valor será desconsiderado e nada será preenchido.
 * </p>
 * <p>
 * É utilizado o método <code>toString()</code> do objeto preenchedor.
 * </p>
 * <p>
 * Exemplo:<br/>
 * <pre>
 * Filler<Integer> filler = new Filler(new Integer(10), SideToFill.LEFT);
 * String outPut = filler.fill("TESTE", 8);
 * 
 * outPut -> "101TESTE"
 * </pre>
 * </p> 
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L</a>
 * @author Misael Barreto
 * @author Rômulo Augusto
 * @author <a href="http://www.nordeste-fomento.com.br">Nordeste Fomento
 *         Mercantil</a>
 * 
 * @since JRimum 1.0
 * 
 * @version 0.2
 *
 */
public class Filler<G> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3996934478582358340L;

	/**
	 * Filler padrão para preenchimento com zeros a esquerda.
	 */
	public static final Filler<Integer> ZERO_LEFT = new Filler<Integer>(0, SideToFill.LEFT);
	
	/**
	 * Filler padrão para preenchimento com zeros a direita.
	 */
	public static final Filler<Integer> ZERO_RIGHT = new Filler<Integer>(0, SideToFill.RIGHT);
	
	/**
	 * Filler padrão para preenchimento com espaços em branco a esquerda.
	 */
	public static final Filler<String> WHITE_SPACE_LEFT = new Filler<String>(StringUtil.WHITE_SPACE, SideToFill.LEFT);
	
	/**
	 * Filler padrão para preenchimento com espaços em branco a direita.
	 */
	public static final Filler<String> WHITE_SPACE_RIGHT = new Filler<String>(StringUtil.WHITE_SPACE, SideToFill.RIGHT);

	/**
	 * 
	 */
	private G fillWith;
	
	/**
	 * 
	 */
	private SideToFill sideToFill;
	
	/**
	 * @param fillWith
	 * @param sideToFill
	 */
	public Filler(G fillWith, SideToFill sideToFill) {
		
		setFillWith(fillWith);
		setSideToFill(sideToFill);
	}
	
	public G getFillWith() {
		return fillWith;
	}

	/**
	 * @param fillWith valor que preenche o real valor do campo
	 */
	public void setFillWith(G fillWith) {
		
		if(isNotNull(fillWith))
			this.fillWith = fillWith;
		
		else
			throw new IllegalArgumentException("Filler inválido [ " + fillWith + " ]!");
	}

	/**
	 * @return enum SideToFill
	 */
	public SideToFill getSideToFill() {
		return sideToFill;
	}

	/**
	 * @param sideToFill enum com a informaçao de qual lado a ser preenchido
	 */
	public void setSideToFill(SideToFill sideToFill) {
		
		if(isNotNull(sideToFill))
			this.sideToFill = sideToFill;
		
		else
			throw new IllegalArgumentException("Lado inválido [ " + sideToFill + " ]!");
	}
	
	/**
	 * <p>
	 * Preenche o campo com o caracter especificado e no lado especificado.
	 * </p>
	 * <p>
	 * Exemplo:
	 * <br/>
	 * Se <code>sideToFill == SideToFill.LEFT</code>, o caracter especificado será adicionado à String
	 * no lado esquerdo até que o campo fique com o tamanho que foi definido.
	 * </p>
	 * 
	 * @param toFill String a ser preenchida
	 * @param length tamanho máximo que a String deve ter depois de preenchida
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 */
	public String fill(String toFill, int length){
		
		String str = null;
		
		switch(sideToFill){
		
			case LEFT:
				str = fillLeft(toFill, length);
				break;
				
			case RIGHT:
				str = fillRight(toFill, length);
				break;
		}
		
		return str;
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(long toFill, int length){
		return fill(String.valueOf(toFill), length);
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(int toFill, int length){
		return fill(String.valueOf(toFill), length);
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(short toFill, int length){
		return fill(String.valueOf(toFill), length);
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(byte toFill, int length){
		return fill(String.valueOf(toFill), length);
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(char toFill, int length){
		return fill(String.valueOf(toFill), length);
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(double toFill, int length){
		return fill(String.valueOf(toFill), length);
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>String.valueOf(toFill)</code>.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(float toFill, int length){
		return fill(String.valueOf(toFill), length);
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>toFill.toString()</code>.
	 * <br/>
	 * </p>
	 * <p>
	 * Caso <code>toFill</code> seja <code>null</code>, o método 
	 * <code>fill(String, int)</code> receberá uma String nula como parâmetro.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(Object toFill, int length){
		
		String toFillTemp = null;
		
		if (isNotNull(toFill)) {
			toFillTemp = toFill.toString();
		}
		
		return fill(toFillTemp, length);
	}
	
	/**
	 * 
	 * <p>
	 * Executa o método <code>fill(String, int)</code> passando o parâmetro
	 * <code>toFill</code> como <code>toFill.write()</code>.
	 * <br/>
	 * </p>
	 * <p>
	 * Caso <code>toFill</code> seja <code>null</code>, o método 
	 * <code>fill(String, int)</code> receberá uma String nula como parâmetro.
	 * </p>
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 * 
	 * @see Filler#fill(String, int)
	 */
	public String fill(TextStream toFill, int length){

		String toFillTemp = null;
		
		if(isNotNull(toFill))
			toFillTemp = toFill.write();
		
		return fill(toFillTemp, length);
	}
	
	/**
	 * Preenche a String a direita com valor do atributo "fillWith"
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 */
	private String fillRight(String toFill, int length) {
		
		return StringUtils.rightPad(toFill, length, fillWith.toString());
	}

	/**
	 * Preenche a String a direita com valor do atributo "fillWith"
	 * 
	 * @param toFill Valor a ser preenchido
	 * @param length tamanho máximo que o valor deve ter depois de preenchido
	 * @return Nova String preenchida de acordo com o preenchedor do objeto até o tamanho especificado
	 */
	private String fillLeft(String toFill, int length) {
		
		return StringUtils.leftPad(toFill, length, fillWith.toString());
	}

	/**
	 * <p>
	 * Lados para preencher.
	 * </p> 
	 */
	public enum SideToFill{
		
		LEFT,
		RIGHT;
	}

	@Override
	public String toString() {
		return ObjectUtil.toString(this);
	}
}
