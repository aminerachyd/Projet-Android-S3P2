package com.inpt.jibmaak.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.inpt.jibmaak.R;
import com.inpt.jibmaak.model.User;
import com.inpt.jibmaak.repository.Resource;
import com.inpt.jibmaak.validators.UserValidation;
import com.inpt.jibmaak.viewmodels.ManageAccountViewModel;

import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;

import static com.inpt.jibmaak.repository.Resource.Operation.DELETE;
import static com.inpt.jibmaak.repository.Resource.Operation.UPDATE;

@AndroidEntryPoint
public class ManageAccountActivity extends AuthenticateActivity {
    protected AlertDialog updateDialog;
    protected AlertDialog deleteDialog;
    protected Button bouton_update;
    protected Button bouton_delete;
    protected Button bouton_deconnecter;
    protected ManageAccountViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        viewModel = new ViewModelProvider(this).get(ManageAccountViewModel.class);
        bouton_update = findViewById(R.id.bouton_update_infos);
        bouton_delete = findViewById(R.id.bouton_delete_compte);
        bouton_deconnecter = findViewById(R.id.bouton_deconnecter);

        bouton_update.setOnClickListener(v -> {
            if (user == null)
                askLogin();
            else
                showDialogUpdate();
        });
        bouton_delete.setOnClickListener(v -> {
            if (user == null) {
                askLogin();
                return;
            }
            AlertDialog.Builder adb = new AlertDialog.Builder(ManageAccountActivity.this);
            deleteDialog = adb.setMessage(R.string.delete_compte_confirm)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        if (prepareAction())
                            viewModel.deleteUser(user.getId());
                    })
                    .setNegativeButton(R.string.annuler, (dialog, which) -> dialog.dismiss())
                    .create();
            deleteDialog.show();
        });
        bouton_deconnecter.setOnClickListener(v -> {
            authManager.logout();
            Toast.makeText(ManageAccountActivity.this,R.string.deconnexion_volontaire,
                    Toast.LENGTH_LONG).show();
            finish();
        });

        viewModel.getOperationData().observe(this, stringResource -> {
            removeWaitingScreen();
            if (stringResource.isConsumed())
                return;
            Resource.Status status = stringResource.getStatus();
            Resource.Operation operation = stringResource.getOperation();
            stringResource.setConsumed(true);
            switch (status){
                case OK:
                    if (operation.equals(UPDATE)){
                        Toast.makeText(ManageAccountActivity.this,R.string.update_ok,Toast.LENGTH_SHORT).show();
                        User userUpdated = viewModel.getUserToUpdate();
                        authManager.updateUser(userUpdated);
                    }
                    else if (operation.equals(DELETE)){
                        Toast.makeText(ManageAccountActivity.this,R.string.delete_ok,Toast.LENGTH_SHORT).show();
                        authManager.logout();
                        finish();
                    }
                    break;
                case UNAUTHORIZED:
                    Toast.makeText(ManageAccountActivity.this,R.string.error_unauthorized,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(ManageAccountActivity.this,R.string.error,Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    public void showDialogUpdate() {
        updateDialog = new AlertDialog.Builder(this).setView(R.layout.dialog_update_infos).create();
        updateDialog.show();
        EditText zone_nom = updateDialog.findViewById(R.id.zone_nom_update);
        EditText zone_prenom = updateDialog.findViewById(R.id.zone_prenom_update);
        EditText zone_telephone = updateDialog.findViewById(R.id.zone_phone_update);
        EditText zone_mail = updateDialog.findViewById(R.id.zone_mail_update);
        SwitchCompat switch_mdp_maj = updateDialog.findViewById(R.id.switch_mdp_update);
        EditText zone_mdp = updateDialog.findViewById(R.id.zone_mdp_update);
        Button bouton_maj = updateDialog.findViewById(R.id.bouton_maj);

        zone_nom.setText(user.getNom());
        zone_prenom.setText(user.getPrenom());
        zone_telephone.setText(user.getTelephone());
        zone_mail.setText(user.getEmail());
        switch_mdp_maj.setOnCheckedChangeListener((buttonView, isChecked) ->
                zone_mdp.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        bouton_maj.setOnClickListener(v -> {
            boolean hasErrors = UserValidation.validate(this,zone_nom,zone_prenom,
                    zone_telephone,zone_mail,switch_mdp_maj.isChecked() ? zone_mdp : null,null);
            if (hasErrors)
                Toast.makeText(this,R.string.error_validation,Toast.LENGTH_SHORT).show();
            else if (prepareAction()){
                // Les champs sont corrects : on peut lancer la requete
                String nom = zone_nom.getText().toString();
                String prenom = zone_prenom.getText().toString();
                String telephone = zone_telephone.getText().toString();
                String mail = zone_mail.getText().toString();
                String mdp = switch_mdp_maj.isChecked() ? zone_mdp.getText().toString() : null;
                HashMap<String,String> body = new HashMap<>();
                User userToUpdate = new User(user.getId(),mail,nom,prenom,telephone);
                body.put("nom",nom);
                body.put("prenom",prenom);
                body.put("email",mail);
                body.put("telephone",telephone);
                if (mdp != null)
                    body.put("password",mdp);
                viewModel.setUserToUpdate(userToUpdate);
                viewModel.updateUser(user.getId(),body);
                updateDialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialogs();
    }

    @Override
    public void onLogout(boolean isUnexpected) {
        super.onLogout(isUnexpected);
        dismissDialogs();
    }

    @Override
    public void onLogin() {
        super.onLogin();
        dismissDialogs();
    }

    @Override
    public void onAskLoginFailed() {
        super.onAskLoginFailed();
        finish();
    }

    @Override
    public String getConsommateurName() {
        return "AuthenticateActivity";
    }

    protected void dismissDialogs(){
        if (updateDialog != null)
            updateDialog.dismiss();
        if (deleteDialog != null)
            deleteDialog.dismiss();
    }
}
