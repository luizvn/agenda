import Header from "@/components/Header";
import "./globals.css";


export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-br">
      <body className="flex flex-col min-h-screen">
        <Header />
        <main className="flex-grow p-8">
          {children}
        </main>
      </body>
    </html>
  );
}
