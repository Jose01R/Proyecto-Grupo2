package domain.btree;

import java.util.ArrayList;
import java.util.List;

/* *
 *
 * @author Profesor Lic. Gilberth Chaves A.
 * BST = Binary Search Tree (Arbol de Búsqueda Binaria)
 * */
public class BST implements Tree {
    private BTreeNode root; //se refiere a la raiz del arbol

    @Override
    public BTreeNode getRoot() {
        return root;
    }


    @Override
    public int size() {
        return size(root);
    }

    private int size(BTreeNode node) {
        if (node == null) return 0;
        return 1 + size(node.left) + size(node.right);
    }


    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element){
        if(node==null) return false;
        else if(util.Utility.compare(node.data, element)==0) return true;
        else if(util.Utility.compare(element, node.data)<0)
            return binarySearch(node.left, element);
        else return binarySearch(node.right, element);
    }
    public Object getLeaf(BTreeNode node){
        Object aux;
        if(node==null) return null;
        else if(node.left==null&&node.right==null) return node.data; //es una hoja
        else{
            aux = getLeaf(node.left); //siga bajando por el subarbol izq
            if(aux==null) aux = getLeaf(node.right);
        }
        return aux;
    }
    @Override
    public void add(Object element) {
        this.root = add(root, element);
    }

    private BTreeNode add(BTreeNode node, Object element){
        if(node==null)
            node = new BTreeNode(element);
        else if(util.Utility.compare(element, node.data)<0)
            node.left = add(node.left, element);
        else if(util.Utility.compare(element, node.data)>0)
            node.right = add(node.right, element);
        return node;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element) throws TreeException{
        if(node!=null){
            if(util.Utility.compare(element, node.data)<0)
                node.left = remove(node.left, element);
            else if(util.Utility.compare(element, node.data)>0)
                node.right = remove(node.right, element);
            else if(util.Utility.compare(node.data, element)==0){
                //caso 1. es un nodo si hijos, es una hoja
                if(node.left==null && node.right==null) return null;
                    //caso 2-a. el nodo solo tien un hijo, el hijo izq
                else if (node.left!=null&&node.right==null) {
                    return node.left;
                } //caso 2-b. el nodo solo tien un hijo, el hijo der
                else if (node.left==null&&node.right!=null) {
                    return node.right;
                }
                //caso 3. el nodo tiene dos hijos
                else{
                    //else if (node.left!=null&&node.right!=null) {
                    /* *
                     * El algoritmo de supresión dice que cuando el nodo a suprimir
                     * tiene 2 hijos, entonces busque una hoja del subarbol derecho
                     * y sustituya la data del nodo a suprimir por la data de esa hoja,
                     * luego elimine esa hojo
                     * */
                    Object value = min(node.right);
                    node.data = value;
                    node.right = remove(node.right, value);
                }
            }
        }
        return node; //retorna el nodo modificado o no
    }

    @Override
    public int height(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return height(root, element, 0);
    }

    //devuelve la altura de un nodo (el número de ancestros)
    private int height(BTreeNode node, Object element, int level){
        if(node==null) return 0;
        else if(util.Utility.compare(node.data, element)==0) return level;
        else return Math.max(height(node.left, element, ++level),
                    height(node.right, element, level));
    }

    // ========================== Altura del árbol =====================================

    @Override
    public int height() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        //return height(root, 0); //opción-1
        return height(root); //opción-2
    }

    //devuelve la altura del árbol (altura máxima de la raíz a
    //cualquier hoja del árbol)
    //opcion-1
    private int height(BTreeNode node, int level){
        if(node==null) return level-1;//se le resta 1 al nivel pq no cuente el nulo
        return Math.max(height(node.left, ++level),
                height(node.right, level));
    }

    //opcion-2
    private int height(BTreeNode node){
        if(node==null) return -1; //retorna valor negativo para eliminar el nivel del nulo
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    @Override
    public Object min() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return min(root);
    }

    private Object min(BTreeNode node){
        if(node.left!=null)
            return min(node.left);
        return node.data;
    }

    @Override
    public Object max() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return max(root);
    }

    private Object max(BTreeNode node){
        if(node.right!=null)
            return max(node.right);
        return node.data;
    }

    @Override
    public String preOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return preOrder(root);
    }

    //recorre el árbol de la forma: nodo-hijo izq-hijo der
    private String preOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result = node.data+" ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return  result;
    }

    @Override
    public String inOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return inOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-nodo-hijo der
    private String inOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = inOrder(node.left);
            result += node.data+" ";
            result += inOrder(node.right);
        }
        return  result;
    }

    //para mostrar todos los elementos existentes
    @Override
    public String postOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return postOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-hijo der-nodo,
    private String postOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data+" ";
        }
        return result;
    }

    // SE CALCULA EL BALANCE DE FACTOR
    private int getBalanceFactor(BTreeNode node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    // SE DETERMINA SI ESTA BALANCEADO
    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(BTreeNode node) {
        if (node == null) return true;

        int balanceFactor = getBalanceFactor(node);
        return Math.abs(balanceFactor) <= 1 &&
                isBalanced(node.left) &&
                isBalanced(node.right);
    }

    @Override
    public String toString() {
        String result = "Binary Search Tree (BST) Content:";
        try {
            result += "\nPreOrder: " + preOrder();
            result += "\n\nInOrder: " + inOrder();
            result += "\n\nPostOrder: " + postOrder();
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //para el test
    //Algoritmo que devuelva el padre del elemento dado en un árbol de búsquedabinaria.
    //father esta ready
    @Override
    public Object father(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");

        return father(root,element);
    }

    private Object father(BTreeNode node, Object element){
        Object father = null;

        if(node==null)
            return null;
        else if(node.left!=null && util.Utility.compare(node.left.data, element)==0){
            father = node.data;
        } else if (node.right!=null && util.Utility.compare(node.right.data, element)==0) {
            father = node.data;
        }else if (util.Utility.compare(element, node.data) < 0) {
            // Si el elemento es menor que los datos del nodo actual, buscar en el subárbol izquierdo.
            return father(node.left, element);
        } else if (util.Utility.compare(element, node.data) > 0) {
            // Si el elemento es mayor que los datos del nodo actual, buscar en el subárbol derecho.
            return father(node.right, element);
        } else {
            // Si element.data == node.data, significa que el 'node' actual es el 'element' que estamos buscando.
            // Un nodo no puede ser su propio padre. Si este es el nodo raíz del árbol, su padre es null.
            Object padre = "El elemento es la raiz, por lo tanto no tiene un padre";
            return padre;
        }

        return father;
    }

    //Algoritmo que devuelva el hermano (izquierdo o derecho) del elemento dado.
    @Override
    public Object brother(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");

        return brother(root,element);
    }

    private Object brother(BTreeNode node, Object element){
        Object brother = null;
        if(node==null)
            return null;
            //preguntar si no tiene hermano, que debe hacerse
        else if(node.left!=null && util.Utility.compare(node.left.data,element)==0){
            //si entra al if, nodo.izq se compara con el elemento y si es, se guarda la data de nodo.der
            if(node.right!=null)
                brother = node.right.data;
        } else if (node.right!=null && util.Utility.compare(node.right.data,element)==0) {
            //sino compara el nodo.der con el elemento y si es, se guarda la data de nodo.izq
            if(node.left!=null)
                brother = node.left.data;
        }else if (util.Utility.compare(element, node.data) < 0) {
            // Si el elemento es menor que los datos del nodo actual, buscar en el subárbol izquierdo.
            return brother(node.left, element);

        } else if (util.Utility.compare(element, node.data) > 0) {
            // Si el elemento es mayor que los datos del nodo actual, buscar en el subárbol derecho.
            return brother(node.right, element);
        }//guarda el nodo hermano, del nodo igual al elemento


        return brother;
    }

    //Algoritmo que devuelva los hijos (uno, dos o ninguno) del elemento dado
    @Override
    public String children(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");

        return children(root,element);
    }

    private String children(BTreeNode node, Object element){
        String children = "Children: \n";

        if(node==null)
            return "No existe el nodo";
        else if(util.Utility.compare(node.data, element) == 0) {
            if (node.left!=null && node.right==null){
                children = "" +node.left.data;
            } else if (node.left==null && node.right!=null) {
                children = "" + node.right.data;
            }else{
                children = node.left.data + " y " + node.right.data;
            }
        }else if (util.Utility.compare(element, node.data) < 0) {
            // Si el elemento es menor que los datos del nodo actual, buscar en el subárbol izquierdo.
            return children(node.left, element);

        } else if (util.Utility.compare(element, node.data) > 0) {
            // Si el elemento es mayor que los datos del nodo actual, buscar en el subárbol derecho.
            return children(node.right, element);
        }

        return children;
    }

    public List<BTreeNode> preOrderNodes() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        List<BTreeNode> list = new ArrayList<>();
        preOrderNodes(root, list);
        return list;
    }

    private void preOrderNodes(BTreeNode node, List<BTreeNode> list) {
        if(node != null) {
            list.add(node);  // Visita el nodo
            preOrderNodes(node.left, list);
            preOrderNodes(node.right, list);
        }
    }

    //Metodos similares a los de ordenamiento pero retornando una lista para facilitar el ordenado en la interfaz grafica
    @Override
    public List<BTreeNode> inOrderNodes() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        List<BTreeNode> list = new ArrayList<>();
        inOrderNodes(root, list);
        return list;
    }

    private void inOrderNodes(BTreeNode node, List<BTreeNode> list) {
        if(node != null) {
            inOrderNodes(node.left, list);
            list.add(node);
            inOrderNodes(node.right, list);
        }
    }

    @Override
    public List<BTreeNode> postOrderNodes() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        List<BTreeNode> list = new ArrayList<>();
        postOrderNodes(root, list);
        return list;
    }

    private void postOrderNodes(BTreeNode node, List<BTreeNode> list) {
        if(node != null) {
            postOrderNodes(node.left, list);
            postOrderNodes(node.right, list);
            list.add(node);
        }
    }
}