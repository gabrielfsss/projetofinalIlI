package com.gabriel.projetoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class telaCadastro extends AppCompatActivity {

    private EditText txtNome, txtEmail, txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        txtNome = findViewById(R.id.txtEmail);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
    }

    public void cadastrarUsuario(View view){
        final String nome = txtNome.getText().toString().trim();
        final String email= txtEmail.getText().toString().trim();
        final String senha = txtSenha.getText().toString().trim();

        if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Usuario usuario = new Usuario();
                        usuario.setId(task.getResult().getUser().getUid());
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setSenha(senha);
                        FirebaseFirestore.getInstance().collection("usuarios").document(usuario.getId()).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Realize Seu Login...", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }else Toast.makeText(getApplicationContext(), "Por favor tente novamente...", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Error", "Cadastrar Auth: "+e.getMessage(), e);
                    Toast.makeText(getApplicationContext(), "Por favor tente novamente...", Toast.LENGTH_LONG).show();
                }
            });
        }else Toast.makeText(getApplicationContext(), "Informe todos os campos!", Toast.LENGTH_LONG).show();
    }
}