package br.com.nordestefomento.jrimum.bopepo.campolivre;

import static br.com.nordestefomento.jrimum.utilix.ObjectUtil.exists;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.ContaBancariaBoleto;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.TipoDeCobranca;
import br.com.nordestefomento.jrimum.domkee.financeiro.banco.febraban.Titulo;
import br.com.nordestefomento.jrimum.vallia.digitoverificador.Modulo;

abstract class AbstractCLUnibanco extends AbstractCampoLivre {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6169577742706045367L;

	protected AbstractCLUnibanco(Integer fieldsLength, Integer stringLength) {
		super(fieldsLength, stringLength);
	}

	static CampoLivre create(Titulo titulo)
			throws NotSuporttedCampoLivreException {

		CampoLivre campoLivre = null;

		ContaBancariaBoleto conta = titulo.getContaBancaria();

		if (exists(conta.getCarteira().getTipoCobranca())) {

			if (conta.getCarteira().getTipoCobranca() == TipoDeCobranca.COM_REGISTRO) 
				campoLivre = new CLUnibancoCobrancaRegistrada(titulo);
			else 
				if(conta.getCarteira().getTipoCobranca() == TipoDeCobranca.SEM_REGISTRO)
					campoLivre = new CLUnibancoCobrancaNaoRegistrada(titulo);
				else
					throw new NotSuporttedCampoLivreException(
					"Não existe suporte para um campo livre do unibanco com a cobrança: "+conta.getCarteira().getTipoCobranca());

		} else {
			throw new NotSuporttedCampoLivreException(
					"Campo livre indeterminado, defina o tipo de cobrança para a carteira usada.");
		}

		return campoLivre;
	}

	/**
	 * <p>
	 * Calcula o dígito verificador para
	 * <em>referência do cliente (cobrança sem registro)</em> e base para
	 * cálculo do <em>super dígito do nosso numero (cobrança com registro)</em>.
	 * </p>
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param numero
	 * @return String dígito
	 * 
	 * @since 0.2
	 */
	String calculeDigitoEmModulo11(String numero) {

		String dv = "";

		int soma = Modulo.calculeSomaSequencialMod11(numero, 2, 9);

		soma *= 10;

		final int resto = soma % 11;

		if (resto == 10 || resto == 0)
			dv = "0";
		else
			dv = "" + resto;

		return dv;

	}

}
