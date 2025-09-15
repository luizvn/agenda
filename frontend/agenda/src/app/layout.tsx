import Header from "@/components/Header";
import "./globals.css";


export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-br">
      <body>
        <Header />
        <main className="p-8">
          {children}
        </main>
      </body>
    </html>
  );
}
