package br.com.rodrigoamora.springemongo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public class Aluno {
	
	private ObjectId id;
	
	private String nome;
	
	private Date dataNascimento;
	
	private Curso curso;
	
	private List<Nota> notas;
	
	private List<Habilidade> habilidades;
	
	private Contato contato;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Nota> getNotas() {
		if (notas == null) {
			notas = new ArrayList<Nota>();
		}
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public List<Habilidade> getHabilidades() {
		if (habilidades == null) {
			habilidades = new ArrayList<Habilidade>();
		}
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public Aluno criarId() {
		setId(new ObjectId());
		return this;
	}

	public Aluno adicionar(Aluno aluno, Habilidade habilidade) {
		List<Habilidade> habilidades = aluno.getHabilidades();
		habilidades.add(habilidade);
		aluno.setHabilidades(habilidades);
		return aluno;
	}

	public Aluno adicionar(Aluno aluno, Nota nota) {
		List<Nota> notas = aluno.getNotas();
		notas.add(nota);
		aluno.setNotas(notas);
		return aluno;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}
	
}
