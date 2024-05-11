package manitsche.projeto.cadprodutos;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import manitsche.projeto.cadprodutos.BDHelper.ProdutosBd;
import manitsche.projeto.cadprodutos.model.Produtos;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    ProdutosBd bdHelper;
    ArrayList<Produtos> listview_Produtos;
    Produtos produto;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCadastrar = findViewById(R.id.btn_Cadastrar);
        lista = findViewById(R.id.listview_Produtos);
        registerForContextMenu(lista);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormularioProdutos.class);
                startActivity(intent);
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Produtos produtoEscolhido = (Produtos) adapter.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, FormularioProdutos.class);
                intent.putExtra("produto-escolhido", produtoEscolhido);
                startActivity(intent);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                produto = (Produtos) adapter.getItemAtPosition(position);
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuDelete = menu.add("Deletar este produto");
        menuDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                bdHelper = new ProdutosBd(MainActivity.this);
                bdHelper.deletarProduto(produto);
                bdHelper.close();
                carregarProduto();
                return true;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        carregarProduto();
    }

    public void carregarProduto() {
        bdHelper = new ProdutosBd(MainActivity.this);
        listview_Produtos = bdHelper.getLista();
        bdHelper.close();

        if (listview_Produtos != null) {
            adapter = new ArrayAdapter<Produtos>(MainActivity.this, android.R.layout.simple_list_item_1, listview_Produtos);
            lista.setAdapter(adapter);
        }

       // finish();
    }
}