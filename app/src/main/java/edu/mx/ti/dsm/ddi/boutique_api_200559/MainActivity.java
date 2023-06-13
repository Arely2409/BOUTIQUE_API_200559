package edu.mx.ti.dsm.ddi.boutique_api_200559;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnEliminar;
    private Button btnActualizar;
    private EditText etCodigo;
    private EditText etTipo;
    private EditText etColor;
    private EditText etMarca;
    private EditText etPrecio;
    private EditText etExistencia;
    private ListView lvProducto;
    private RequestQueue Peticiones;
    private JsonArrayRequest jsonArrayRequest;
    private ArrayList<String> Datos = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    //private String url = "http://192.168.1.79:3300";
    private String url = "http://192.168.1.70:3300";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnActualizar);
        etCodigo = findViewById(R.id.etCodigo);
        etTipo = findViewById(R.id.etTipo);
        etColor = findViewById(R.id.etColor);
        etMarca = findViewById(R.id.etMarca);
        etPrecio = findViewById(R.id.etPrecio);
        etExistencia = findViewById(R.id.etExistencia);
        Peticiones = Volley.newRequestQueue(this);
        lvProducto = findViewById(R.id.lvProducto);
        listaDeProductos();


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCodigo.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingresa el código", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.DELETE,
                            url + "/eliminar/" + etCodigo.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Producto Eliminado")) {
                                            Toast.makeText(MainActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                                            etCodigo.setText("");
                                            etTipo.setText("");
                                            etColor.setText("");
                                            etMarca.setText("");
                                            etPrecio.setText("");
                                            etExistencia.setText("");
                                            adapter.clear();
                                            lvProducto.setAdapter(adapter);
                                            listaDeProductos();
                                        } else if (response.getString("status").equals("No Encontrado")) {
                                            Toast.makeText(MainActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    Peticiones.add(jsonObjectRequest);
                }
            }
        });


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCodigo.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingresa el código", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObjectRequest peticion = new JsonObjectRequest(
                            Request.Method.GET,
                            url + "/" + etCodigo.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if (response.has("status"))
                                        Toast.makeText(MainActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                                    else {
                                        try {
                                            etTipo.setText(response.getString("tipo"));
                                            etColor.setText(response.getString("color"));
                                            etMarca.setText(response.getString("marca"));
                                            etPrecio.setText(String.valueOf(response.getInt("precio")));
                                            etExistencia.setText(String.valueOf(response.getInt("existencia")));
                                        } catch (JSONException e) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                    );

                    Peticiones.add(peticion);
                }
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCodigo.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Llena los campos para insertar un producto", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject producto = new JSONObject();
                    try {
                        producto.put("codigo", etCodigo.getText().toString());
                        producto.put("tipo", etTipo.getText().toString());
                        producto.put("color", etColor.getText().toString());
                        producto.put("marca", etMarca.getText().toString());
                        producto.put("precio", Float.parseFloat(etPrecio.getText().toString()));
                        producto.put("existencia", Float.parseFloat(etExistencia.getText().toString()));
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url + "/insertar",
                            producto,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Producto insertado")) {
                                            Toast.makeText(MainActivity.this, "Producto insertado", Toast.LENGTH_SHORT).show();
                                            etCodigo.setText("");
                                            etTipo.setText("");
                                            etColor.setText("");
                                            etMarca.setText("");
                                            etPrecio.setText("");
                                            etExistencia.setText("");
                                            adapter.clear();
                                            lvProducto.setAdapter(adapter);
                                            listaDeProductos();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    Peticiones.add(jsonObjectRequest);
                }
            }
        });


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = etCodigo.getText().toString();
                if (codigo.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingresa el código", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject productos = new JSONObject();
                        productos.put("codigo", codigo);
                        if (!etTipo.getText().toString().isEmpty()) {
                            productos.put("tipo", etTipo.getText().toString());
                        }
                        if (!etColor.getText().toString().isEmpty()) {
                            productos.put("color", etColor.getText().toString());
                        }
                        if (!etMarca.getText().toString().isEmpty()) {
                            productos.put("marca", etMarca.getText().toString());
                        }
                        if (!etPrecio.getText().toString().isEmpty()) {
                            productos.put("precio", Float.parseFloat(etPrecio.getText().toString()));
                        }
                        if (!etExistencia.getText().toString().isEmpty()) {
                            productos.put("existencia", Float.parseFloat(etExistencia.getText().toString()));
                        }
                        JsonObjectRequest actualizar = new JsonObjectRequest(
                                Request.Method.PUT,
                                url + "/actualizar/" + codigo,
                                productos,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.has("status")) {
                                                String status = response.getString("status");
                                                if (status.equals("Producto Actualizado")) {
                                                    if (productos.length() > 1) {
                                                        Toast.makeText(MainActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                                                        etCodigo.setText("");
                                                        etTipo.setText("");
                                                        etColor.setText("");
                                                        etMarca.setText("");
                                                        etPrecio.setText("");
                                                        etExistencia.setText("");
                                                        adapter.clear();
                                                        lvProducto.setAdapter(adapter);
                                                        listaDeProductos();
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else if (status.equals("No Encontrado")) {
                                                    Toast.makeText(MainActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                        Peticiones.add(actualizar);
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    protected void listaDeProductos(){
        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0 ; i<response.length();i++){
                            try {
                                String codigo = response.getJSONObject(i).getString("codigo");
                                String tipo = response.getJSONObject(i).getString("tipo");
                                String color = response.getJSONObject(i).getString("color");
                                String marca = response.getJSONObject(i).getString("marca");
                                String precio = response.getJSONObject(i).getString("precio");
                                Datos.add(codigo+": "+tipo+", "+color+", "+marca+", "+precio);
                            } catch (JSONException e) {

                            }
                        }
                        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Datos);
                        lvProducto.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Peticiones.add(jsonArrayRequest);
    }
}