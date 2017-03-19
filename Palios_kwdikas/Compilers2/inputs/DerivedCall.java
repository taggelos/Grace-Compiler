class DerivedCall {
	public static void main(String[] x) {
		int i;
		B b;
		F f;
		f = new F();
		b = new B();
		i = f.foo(b);
		System.out.println(i);
	}
}


