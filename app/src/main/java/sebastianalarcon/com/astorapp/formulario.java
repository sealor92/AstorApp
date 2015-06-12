package sebastianalarcon.com.astorapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class formulario extends ActionBarActivity implements View.OnClickListener {

    DataBaseManager Manager = MainActivity.getManager();
    private Cursor cursor;
    private ListView lista;
    private SimpleCursorAdapter adapter;
    private EditText Ednombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        lista = (ListView) findViewById(android.R.id.list);
        Ednombre = (EditText) findViewById(R.id.EdText1);

        String[] from = new String[]{Manager.CN_ID,Manager.CN_NAME};
        int[] to = new int[]{android.R.id.text1,android.R.id.text2};
        cursor = Manager.cargarCursorContactos();
        adapter = new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,from,to,0);
        lista.setAdapter(adapter);

        //Manager.insertar("Alejo","5822128");
        //Manager.insertar("Pablo","2651752");
        //Manager.insertar("Paula","4910413");
        Button btnbuscar = (Button) findViewById(R.id.btn1);
        btnbuscar.setOnClickListener(this);
        Button btncargar = (Button) findViewById(R.id.btndb);
        btncargar.setOnClickListener(this);
        Button btninsertar = (Button) findViewById(R.id.btninsertar);
        btninsertar.setOnClickListener(this);
        Button btneliminar = (Button) findViewById(R.id.btneliminar);
        btneliminar.setOnClickListener(this);
        Button btnactualizar = (Button) findViewById(R.id.btnactualizar);
        btnactualizar.setOnClickListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mapa) {
            Intent m = new Intent(this,mapa.class);
            startActivity(m);
            return true;
        }
        if (id == R.id.main) {
            Intent ma = new Intent(this,MainActivity.class);
            startActivity(ma);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn1){
            new BuscarTask().execute();
        }
        if(v.getId()==R.id.btndb){
            lista = (ListView) findViewById(android.R.id.list);
            Ednombre = (EditText) findViewById(R.id.EdText1);

            String[] from = new String[]{Manager.CN_NAME,Manager.CN_LAT};
            int[] to = new int[]{android.R.id.text1,android.R.id.text2};
            cursor = Manager.cargarCursorContactos();
            adapter = new SimpleCursorAdapter(this,android.R.layout.two_line_list_item,cursor,from,to,0);
            lista.setAdapter(adapter);

        }
        if (v.getId()==R.id.btninsertar){
            EditText nombre = (EditText) findViewById(R.id.EdNombre);
            EditText latitud = (EditText) findViewById(R.id.EdLatitud);
            EditText longitud = (EditText) findViewById(R.id.EdLong);
            Manager.insertar(nombre.getText().toString(),latitud.getText().toString(),longitud.getText().toString());
            nombre.setText("");
            latitud.setText("");
            longitud.setText("");
            Toast.makeText(getApplicationContext(), "Insertado", Toast.LENGTH_SHORT).show();
        }
        if(v.getId()==R.id.btneliminar){
            EditText nombre = (EditText) findViewById(R.id.EdNombre);
            Manager.eliminar(nombre.getText().toString());
            Toast.makeText(getApplicationContext(),"Eliminado", Toast.LENGTH_SHORT).show();
            nombre.setText("");
        }
        if (v.getId()==R.id.btnactualizar){
            EditText nombre = (EditText) findViewById(R.id.EdNombre);
            EditText latitud = (EditText) findViewById(R.id.EdLatitud);
            EditText longitud = (EditText) findViewById(R.id.EdLong);
            Manager.Modificar(nombre.getText().toString(),latitud.getText().toString(),longitud.getText().toString());
            Toast.makeText(getApplicationContext(),"Actualizado", Toast.LENGTH_SHORT).show();
            nombre.setText("");
            latitud.setText("");
            longitud.setText("");
        }
    }


    private class BuscarTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),"Buscando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            cursor = Manager.buscarContacto(Ednombre.getText().toString());
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(),"Finalizado", Toast.LENGTH_SHORT).show();
            adapter.changeCursor(cursor);
            obtener();
        }


    }

    public void obtener () {
        TextView Txnombre = (TextView) findViewById(R.id.Txnombre);
        TextView Txlatitud = (TextView) findViewById(R.id.TxLatitud);
        TextView Txlongitud = (TextView) findViewById(R.id.TxLongitud);
        if (cursor.moveToFirst()){
            String dbnombre = cursor.getString(cursor.getColumnIndex(Manager.CN_NAME));
            Txnombre.setText(dbnombre);
            String dblatitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LAT));
            Txlatitud.setText(dblatitud);
            String dblongitud = cursor.getString(cursor.getColumnIndex(Manager.CN_LONG));
            Txlongitud.setText(dblongitud);}
        else{
            Txnombre.setText("No Found");
            Txlatitud.setText("No Found");
            Txlongitud.setText("No Found");
        }

    }
}