package com.inpt.jibmaak.repository;

import androidx.lifecycle.MutableLiveData;

import com.inpt.jibmaak.repository.Resource.Operation;
import com.inpt.jibmaak.services.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.inpt.jibmaak.repository.Resource.Status.ERROR;
import static com.inpt.jibmaak.repository.Resource.Status.OK;
import static com.inpt.jibmaak.repository.Resource.Status.REQUEST_ERROR;
import static com.inpt.jibmaak.repository.Resource.Status.SERVER_ERROR;
import static com.inpt.jibmaak.repository.Resource.Status.UNAUTHORIZED;

/** Callback appelé lorsqu'on recoit une réponse du serveur à une opération de crud */
public class CrudCallback<T> implements Callback<ServerResponse<T>> {
    protected Operation operation;
    protected MutableLiveData<Resource<T>> resultLiveData;
    public CrudCallback(Operation operation, MutableLiveData<Resource<T>> resultLiveData){
        this.operation = operation;
        this.resultLiveData = resultLiveData;
    }
    @Override
    public void onResponse(Call<ServerResponse<T>> call, Response<ServerResponse<T>> response) {
        Resource<T> result = new Resource<>();
        result.setOperation(operation);
        if (!response.isSuccessful()){
            switch (response.code()){
                case 400:
                    result.setStatus(REQUEST_ERROR);
                    break;
                case 401:
                    result.setStatus(UNAUTHORIZED);
                    break;
                default:
                    result.setStatus(SERVER_ERROR);
            }
        }
        else {
            result.setStatus(OK);
            result.setResource(response.body().getPayload());
        }
        resultLiveData.setValue(result);
    }

    @Override
    public void onFailure(Call<ServerResponse<T>> call, Throwable t) {
        Resource<T> result = new Resource<>();
        result.setStatus(ERROR);
        result.setOperation(operation);
        resultLiveData.setValue(result);
    }
}